package io.fajarca.feature.domain.repository

import io.fajarca.core.common.Result
import io.fajarca.feature.domain.CharacterDetail

interface CharacterDetailRepository {
    suspend fun getCharacterDetail(characterId : Int) : Result<CharacterDetail>
}


