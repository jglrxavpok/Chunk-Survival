package org.jglrxavpok.chunksurvival

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.Logger

@Mod.EventBusSubscriber
@Mod(modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter", modid = ChunkSurvival.ModID, dependencies = "required-after:forgelin;",
        name = "Chunk Survival", version = "3.0", updateJSON = "https://raw.githubusercontent.com/jglrxavpok/Chunk-Survival/master/updateCheck.json")
object ChunkSurvival {


    const val ModID = "chunksurvival"

    lateinit var logger: Logger

    lateinit var config: Configuration

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        logger = event.modLog
        logger.info("Load world type $ChunkSurvivalWorldType") // this line **loads** the world type and therefore registers it
        config = Configuration(event.suggestedConfigurationFile)
        MinecraftForge.EVENT_BUS.register(this)
    }

}