package di

import SecureMessengerServer
import org.koin.dsl.module
import com.google.gson.GsonBuilder
import controllers.*
import repositories.*
import models.Channel
import adapters.ChannelTypeAdapter
import database.TempStorage

val appModule = module {
    single {
        GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Channel::class.java, ChannelTypeAdapter())
            .create()
    }

    single { TempStorage() }

    single<ChatsRepository> { ChatsRepositoryImpl(get()) }

    single { ChatsController(get(), get()) }

    single<ChannelsController> { ChannelsControllerImpl(get()) }

    single { SecureMessengerServer(get(), get()) }
}