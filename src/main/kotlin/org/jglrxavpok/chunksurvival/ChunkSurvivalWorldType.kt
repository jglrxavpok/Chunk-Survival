package org.jglrxavpok.chunksurvival

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiCreateWorld
import net.minecraft.world.World
import net.minecraft.world.WorldType
import net.minecraft.world.biome.BiomeProvider
import net.minecraft.world.gen.IChunkGenerator
import org.jglrxavpok.chunksurvival.client.GuiCustomizeChunkSurvival

object ChunkSurvivalWorldType: WorldType("chunk_survival") {

    private var gson = Gson()

    data class Options(var clusterSize: Int = 1, var spacing: Int = 1, var spawnLava: Boolean = true,
                       var randomChunks: Boolean = false, var probability: Double = 0.5) {

        fun fromJsonString(json: String) {
            try {
                val obj = gson.fromJson(json, JsonObject::class.java) ?: return
                clusterSize = obj["clusterSize"]?.asInt ?: 1
                spacing = obj["spacing"]?.asInt ?: 1
                spawnLava = obj["spawnLava"]?.asBoolean ?: true
                randomChunks = obj["randomChunks"]?.asBoolean ?: false
                probability = obj["probability"]?.asDouble ?: 0.5
            } catch (e: JsonSyntaxException) {
                ChunkSurvival.logger.error("Failed to load ChunkSurvival world type options from string: $json")
                e.printStackTrace()
            }
        }

        fun toJsonString(): String {
            val obj = JsonObject()
            obj.addProperty("clusterSize", clusterSize)
            obj.addProperty("spacing", spacing)
            obj.addProperty("spawnLava", spawnLava)
            obj.addProperty("randomChunks", randomChunks)
            obj.addProperty("probability", probability)
            return gson.toJson(obj)
        }
    }

    override fun getChunkGenerator(world: World, generatorOptions: String): IChunkGenerator {
        return ChunkGeneratorChunkSurvival(world, world.seed, world.worldInfo.isMapFeaturesEnabled, generatorOptions)
    }

    override fun getBiomeProvider(world: World?): BiomeProvider {
        return super.getBiomeProvider(world)
    }

    override fun isCustomizable(): Boolean {
        return true
    }

    override fun onCustomizeButton(mc: Minecraft, guiCreateWorld: GuiCreateWorld) {
        mc.displayGuiScreen(GuiCustomizeChunkSurvival(guiCreateWorld, guiCreateWorld.chunkProviderSettingsJson))
    }
}