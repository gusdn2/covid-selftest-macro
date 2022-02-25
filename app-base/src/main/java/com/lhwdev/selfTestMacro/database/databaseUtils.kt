package com.lhwdev.selfTestMacro.database

import android.content.SharedPreferences
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.runtime.snapshots.StateRecord
import androidx.compose.runtime.snapshots.withCurrent
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.core.content.edit
import com.lhwdev.selfTestMacro.utils.SynchronizedMutableStateImpl
import com.lhwdev.selfTestMacro.utils.sEmpty
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json


interface PreferenceItemState<T> : SnapshotMutableState<T>, PreferenceHolder.Property {
	fun forceWrite()
}


abstract class PreferenceItemStateImpl<T>(protected val holder: PreferenceHolder, key: String) :
	SynchronizedMutableStateImpl<T>(structuralEqualityPolicy()), PreferenceItemState<T> {
	@Suppress("LeakingThis")
	override var next: StateStateRecord<T> = StateStateRecordImpl(this)
	
	override fun onPropertyUpdated() {
		next.withCurrent { it.emptyCache() }
	}
	
	override fun forceWrite() {
		next.withCurrent { it.apply() }
	}
	
	override fun prependStateRecord(value: StateRecord) {
		@Suppress("UNCHECKED_CAST")
		next = value as StateStateRecord<T>
	}
	
	abstract fun read(): T
	abstract fun write(value: T)
	
	private class StateStateRecordImpl<T>(val state: PreferenceItemStateImpl<T>, cache: Any? = sEmpty) :
		StateStateRecord<T>(cache) {
		override fun create(): StateRecord = StateStateRecordImpl(state, cache)
		
		override fun read(): T = state.read()
		
		override fun write(value: T) {
			state.write(value)
		}
	}
}

inline fun <T> PreferenceHolder.preferenceState(
	key: String,
	crossinline read: (SharedPreferences) -> T,
	crossinline write: (SharedPreferences, T) -> Unit
): PreferenceItemState<T> = property<PreferenceItemState<T>>(key) {
	object : PreferenceItemStateImpl<T>(holder = this, key = key) {
		override fun read(): T = read(holder.pref)
		
		override fun write(value: T) {
			write(holder.pref, value)
		}
	}
}


fun PreferenceHolder.preferenceInt(
	key: String, defaultValue: Int
): PreferenceItemState<Int> = preferenceState(
	key = key,
	read = { pref -> pref.getInt(key, defaultValue) },
	write = { pref, value -> pref.edit { putInt(key, value) } }
)

fun PreferenceHolder.preferenceLong(
	key: String, defaultValue: Long
): PreferenceItemState<Long> = preferenceState(
	key = key,
	read = { pref -> pref.getLong(key, defaultValue) },
	write = { pref, value -> pref.edit { putLong(key, value) } }
)

fun PreferenceHolder.preferenceBoolean(
	key: String, defaultValue: Boolean
): PreferenceItemState<Boolean> = preferenceState(
	key = key,
	read = { pref -> pref.getBoolean(key, defaultValue) },
	write = { pref, value -> pref.edit { putBoolean(key, value) } }
)

fun PreferenceHolder.preferenceString(
	key: String, defaultValue: String? = null
): PreferenceItemState<String?> = preferenceState(
	key = key,
	read = { pref -> pref.getString(key, defaultValue) },
	write = { pref, value -> pref.edit { putString(key, value) } }
)

fun PreferenceHolder.preferenceStringSet(
	key: String, defaultValue: Set<String>
): PreferenceItemState<Set<String>> = preferenceState(
	key = key,
	read = { pref -> pref.getStringSet(key, defaultValue)!! },
	write = { pref, value -> pref.edit { putStringSet(key, value) } }
)

@OptIn(ExperimentalSerializationApi::class)
fun <T> PreferenceHolder.preferenceSerialized(
	key: String,
	serializer: KSerializer<T>,
	defaultValue: T,
	formatter: StringFormat = Json
): PreferenceItemState<T> = preferenceState(
	key = key,
	read = { pref ->
		val string = pref.getString(key, null)
		if(string == null) {
			defaultValue
		} else {
			formatter.decodeFromString(serializer, string)
		}
	},
	write = { pref, value ->
		pref.edit {
			putString(key, formatter.encodeToString(serializer, value))
		}
	}
)
