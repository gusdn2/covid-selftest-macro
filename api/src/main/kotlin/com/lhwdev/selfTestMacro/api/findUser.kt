@file:Suppress("SpellCheckingInspection", "INVISIBLE_REFERENCE")

package com.lhwdev.selfTestMacro.api

import com.lhwdev.fetch.Bodies
import com.lhwdev.fetch.http.HttpMethod
import com.lhwdev.fetch.http.Session
import com.lhwdev.fetch.http.fetch
import com.lhwdev.fetch.jsonObject
import com.lhwdev.fetch.get
import com.lhwdev.fetch.sDefaultFakeHeader
import com.lhwdev.selfTestMacro.toJsonLoose


// TODO: multiple users with same institute, name, birthday, loginType (that exists in hcs code)
public suspend fun Session.findUser(
	institute: InstituteInfo,
	name: String,
	birthday: String,
	loginType: LoginType,
	pageNumber: Int? = null
): UsersIdentifier = fetch(
	institute.requestUrl2["findUser"],
	method = HttpMethod.post,
	headers = sDefaultFakeHeader,
	body = Bodies.jsonObject {
		"orgCode" set institute.code
		"name" set encrypt(name)
		"birthday" set encrypt(birthday)
		"stdntPNo" set pageNumber
		"loginType" set loginType.name
	}
).toJsonLoose(UsersIdentifier.serializer())
