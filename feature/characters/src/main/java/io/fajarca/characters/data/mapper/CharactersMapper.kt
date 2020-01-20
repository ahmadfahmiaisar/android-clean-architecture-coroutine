package io.fajarca.characters.data.mapper

import io.fajarca.characters.data.response.CharacterDto
import io.fajarca.core.mapper.Mapper
import io.fajarca.core.database.CharacterEntity
import io.fajarca.characters.domain.MarvelCharacter

class CharactersMapper : Mapper<CharacterDto, List<CharacterEntity>>(){

    override fun map(input: CharacterDto): List<CharacterEntity>{
        val chars = mutableListOf<CharacterEntity>()
        input.data.results.forEach {
            chars.add(CharacterEntity(it.id, it.name, "${it.thumbnail.path}/portrait_uncanny.${it.thumbnail.extension}"))
        }
        return chars
    }

    fun mapToDomain(characters : List<CharacterEntity>) : List<MarvelCharacter> {
        val chars = mutableListOf<MarvelCharacter>()
        characters.map {
            chars.add(MarvelCharacter(it.id, it.title, it.imageUrl))
        }
        return chars
    }
}