package di

import server.SecureMessengerServer
import org.koin.dsl.module
import com.google.gson.GsonBuilder
import controllers.*
import repositories.*
import models.*
import adapters.*
import database.TempStorage

val appModule = module {
    single {
        GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Data::class.java, DataTypeAdapter())
            .registerTypeAdapter(Channel::class.java, ChannelTypeAdapter())
            .registerTypeAdapter(ChatStatus::class.java, ChatStatusTypeAdapter())
            .create()
    }

    single { TempStorage() }

    single<UsersRepository> { UsersRepositoryImpl(get()) }

    single<ChatsRepository> { ChatsRepositoryImpl(get()) }

    single { AuthController(get(), get()) }

    single { UsersController(get()) }

    single { ChatsController(get()) }

    single { ChatController(get()) }

    single { SecureMessengerServer(get(), get(), get(), get(), get()) }
}