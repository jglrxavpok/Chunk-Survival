package org.jglrxavpok.chunksurvival

import net.minecraft.init.Biomes
import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.Chunk.NULL_BLOCK_STORAGE
import net.minecraft.world.chunk.storage.ExtendedBlockStorage
import net.minecraft.world.gen.ChunkGeneratorOverworld

class ChunkGeneratorChunkSurvival(val worldIn: World, seed: Long, mapFeaturesEnabledIn: Boolean, generatorOptions: String): ChunkGeneratorOverworld(worldIn, seed, mapFeaturesEnabledIn, generatorOptions) {

    val options = ChunkSurvivalWorldType.Options()
    init {
        options.fromJsonString(generatorOptions)
    }

    private fun shouldFillChunk(x: Int, z: Int): Boolean {
        if(options.randomChunks) {
            return Math.random() < options.probability
        }
        var xOffset = x
        var zOffset = z
        val width = options.spacing+options.clusterSize

        // avoid duplication near 0 coordinates
        if(xOffset < 0) {
            xOffset = -xOffset + options.clusterSize
        }
        if(zOffset < 0) {
            zOffset = -zOffset + options.clusterSize
        }

        xOffset %= width
        zOffset %= width

        return xOffset < options.clusterSize && zOffset < options.clusterSize
    }

    override fun generateChunk(x: Int, z: Int): Chunk {
        if(!shouldFillChunk(x, z)) {
            val chunk = Chunk(worldIn, x, z)
            chunk.biomeArray.fill(Biome.getIdForBiome(Biomes.PLAINS).toByte())

            if(options.spawnLava) {
                val flag = worldIn.provider.hasSkyLight()
                for (chunkX in 0..15) {
                    for (chunkZ in 0..15) {
                        for (y in 0..1) {
                            val iblockstate = if(y == 0) Blocks.BEDROCK.defaultState else Blocks.LAVA.defaultState

                            val index = y shr 4

                            if (chunk.blockStorageArray[index] === NULL_BLOCK_STORAGE) {
                                chunk.blockStorageArray[index] = ExtendedBlockStorage(index shl 4, flag)
                            }

                            chunk.blockStorageArray[index].set(chunkX, y and 15, chunkZ, iblockstate)
                        }
                    }
                }
            }
            chunk.generateSkylightMap()
            return chunk
        }
        return super.generateChunk(x, z)
    }
}