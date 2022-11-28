package com.droident.saltpaymusic.data.repository

import app.cash.turbine.test
import com.droident.saltpaymusic.data.local.AlbumEntity
import com.droident.saltpaymusic.data.local.MusicDao
import com.droident.saltpaymusic.data.local.MusicDaoFake
import com.droident.saltpaymusic.data.local.MusicDatabase
import com.droident.saltpaymusic.data.mapper.toAlbum
import com.droident.saltpaymusic.data.remote.MusicApi
import com.droident.saltpaymusic.data.remote.dto.*
import com.droident.saltpaymusic.util.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MusicRepositoryImplTest {

    private val attributes = Attributes(
        label = "",
        imId = 0,
        currency = "",
        amount = ""
    )

    private val albumDto = AlbumDto(
        title = Title(label = "test-title"),
        name = Name(label = "test-name"),
        price = Price(attributes = attributes, label = "test-price"),
        artist = Artist(label = "test-author"),
        image = listOf(Image(attributes = attributes, label = "test-image")),
        id = Id(attributes, label = "")
    )


    private val albumResponse = AlbumResponse(
        Feed(albums = listOf(
            albumDto,
            albumDto,
            albumDto
        )
        ))

    private lateinit var repository: MusicRepositoryImpl
    private lateinit var api: MusicApi
    private lateinit var db: MusicDatabase
    private lateinit var musicDao: MusicDao


    @Before
    fun setUp() {
        api = mockk(relaxed =  true) {
            coEvery { getAlbums() } returns albumResponse
        }
        musicDao = MusicDaoFake()
        db = mockk(relaxed = true) {
            every { dao } returns musicDao
        }

        repository = MusicRepositoryImpl(
            api = api,
            db = db,
        )
    }

    @Test
    fun `Test local database cache with fetch from remote set to true`() = runTest {
        val localListings = listOf(AlbumEntity(name = "test-title",
            author = "test-author",
            price = "test-price",
            currency = "test-currency",
            image = "test-image",
            albumId = 0))
        musicDao.insertAlbums(localListings)

        repository.getAlbums(fetchFromRemote = true, query = "").test {
            val startLoading = awaitItem()
            assertThat((startLoading as Resource.Loading).isLoading).isTrue()

            val listingsFromDb = awaitItem()
            assertThat(listingsFromDb is Resource.Success).isTrue()
            assertThat(listingsFromDb.data).isEqualTo(localListings.map { it.toAlbum() })

            val remoteListingsFromDb = awaitItem()
            assertThat(remoteListingsFromDb is Resource.Success).isTrue()
            assertThat(remoteListingsFromDb.data).isEqualTo(musicDao.searchAlbums("")
                .map { it.toAlbum() })

            val stopLoading = awaitItem()
            assertThat((stopLoading as Resource.Loading).isLoading).isFalse()

            awaitComplete()
        }
    }
}