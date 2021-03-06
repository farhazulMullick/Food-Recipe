
package com.example.foodrecipe.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import com.example.foodrecipe.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodrecipe.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodrecipe.util.Constants.Companion.PREFERENCE_BACK_ONLINE
import com.example.foodrecipe.util.Constants.Companion.PREFERENCE_DIET_TYPE
import com.example.foodrecipe.util.Constants.Companion.PREFERENCE_DIET_TYPE_ID
import com.example.foodrecipe.util.Constants.Companion.PREFERENCE_MEAL_TYPE
import com.example.foodrecipe.util.Constants.Companion.PREFERENCE_MEAL_TYPE_ID
import com.example.foodrecipe.util.Constants.Companion.PREFERENCE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(
        @ApplicationContext private val context: Context) {

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCE_NAME
    )

    private object PreferenceKeys{
        val SELECTED_MEAL_TYPE = stringPreferencesKey(PREFERENCE_MEAL_TYPE)
        val SELECTED_MEAL_TYPE_ID = intPreferencesKey(PREFERENCE_MEAL_TYPE_ID)
        val SELECTED_DIET_TYPE = stringPreferencesKey(PREFERENCE_DIET_TYPE)
        val SELECTED_DIET_TYPE_ID = intPreferencesKey(PREFERENCE_DIET_TYPE_ID)
        val BACK_ONLINE = booleanPreferencesKey(PREFERENCE_BACK_ONLINE)
    }

    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
            .catch {exception ->
                if(exception is IOException) {
                    emit(emptyPreferences())
                }else{
                    throw exception
                }
            }
            .map { Preferences ->
                val selectedMealType = Preferences[PreferenceKeys.SELECTED_MEAL_TYPE] ?: DEFAULT_MEAL_TYPE
                val selectedMealTypeId = Preferences[PreferenceKeys.SELECTED_MEAL_TYPE_ID] ?: 0
                val selectedDietType = Preferences[PreferenceKeys.SELECTED_DIET_TYPE] ?: DEFAULT_DIET_TYPE
                val selectedDietTypeId = Preferences[PreferenceKeys.SELECTED_DIET_TYPE_ID] ?: 0

                MealAndDietType(
                        selectedMealType,
                        selectedMealTypeId,
                        selectedDietType,
                        selectedDietTypeId
                )

            }

    val readBackOnline: Flow<Boolean> = dataStore.data
            .catch {exception ->
                if(exception is IOException) {
                    emit(emptyPreferences())
                }else{
                    throw exception
                }
            }
            .map {Preferences ->
                val backOnline = Preferences[PreferenceKeys.BACK_ONLINE] ?: false
                backOnline
            }

    suspend fun writeMealAndDietType(
            selectedMealType: String,
            selectedMealTypeId: Int,
            selectedDietType: String,
            selectedDietTypeId: Int
    ){
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferenceKeys.SELECTED_MEAL_TYPE] = selectedMealType
            mutablePreferences[PreferenceKeys.SELECTED_MEAL_TYPE_ID] = selectedMealTypeId
            mutablePreferences[PreferenceKeys.SELECTED_DIET_TYPE] = selectedDietType
            mutablePreferences[PreferenceKeys.SELECTED_DIET_TYPE_ID] = selectedDietTypeId

        }
    }

    suspend fun writeBackOnline(
            backOnline: Boolean
    ){
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferenceKeys.BACK_ONLINE] = backOnline
        }
    }



    data class MealAndDietType(
            val selectedMealType: String,
            val selectedMealTypeId: Int,
            val selectedDietType: String,
            val selectedDietTypeId: Int
    )
}

