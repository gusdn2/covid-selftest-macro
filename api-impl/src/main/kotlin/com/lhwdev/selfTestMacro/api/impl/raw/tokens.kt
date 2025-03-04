package com.lhwdev.selfTestMacro.api.impl.raw

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable(UsersToken.Serializer::class)
public data class UsersToken(val token: String) {
	public object Serializer : KSerializer<UsersToken> {
		override val descriptor: SerialDescriptor =
			PrimitiveSerialDescriptor(UsersToken::class.java.name, PrimitiveKind.STRING)
		
		override fun deserialize(decoder: Decoder): UsersToken = UsersToken(decoder.decodeString())
		override fun serialize(encoder: Encoder, value: UsersToken) {
			encoder.encodeString(value.token)
		}
	}
}


@Serializable(UserToken.Serializer::class)
public data class UserToken(val token: String) {
	public object Serializer : KSerializer<UserToken> {
		override val descriptor: SerialDescriptor =
			PrimitiveSerialDescriptor(UserToken::class.java.name, PrimitiveKind.STRING)
		
		override fun deserialize(decoder: Decoder): UserToken = UserToken(decoder.decodeString())
		override fun serialize(encoder: Encoder, value: UserToken) {
			encoder.encodeString(value.token)
		}
	}
}
