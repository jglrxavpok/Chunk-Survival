package org.jglrxavpok.chunksurvival.client

import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiCreateWorld
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.fml.client.config.GuiCheckBox
import net.minecraftforge.fml.client.config.GuiSlider
import org.jglrxavpok.chunksurvival.ChunkSurvivalWorldType

class GuiCustomizeChunkSurvival(val parent: GuiCreateWorld, val optionsJson: String): GuiScreen() {

    val options = ChunkSurvivalWorldType.Options()
    val title = TextComponentTranslation("chunk_survival.customize.title")
    val sizeText = TextComponentTranslation("chunk_survival.customize.size")
    val spacingText = TextComponentTranslation("chunk_survival.customize.spacing")
    val doneText = TextComponentTranslation("gui.done")
    val cancelText = TextComponentTranslation("gui.cancel")
    val lavaText = TextComponentTranslation("chunk_survival.customize.lava")
    val randomChunksText = TextComponentTranslation("chunk_survival.customize.randomChunks")
    val chunkGridText = TextComponentTranslation("chunk_survival.customize.chunkGrid")
    val probabilityText = TextComponentTranslation("chunk_survival.customize.probability")

    var ID = 0
    val cancelButton = GuiButton(ID++, 0, 0, cancelText.unformattedText)
    val doneButton = GuiButton(ID++, 0, 0, doneText.unformattedText)
    val sliderCallback = GuiSlider.ISlider { }
    val sizeSlider = GuiSlider(ID++, 0, 0, sizeText.unformattedText+": ", 1.0, 100.0, 1.0, sliderCallback)
    val spacingSlider = GuiSlider(ID++, 0, 0, 200, 20, spacingText.unformattedText+": ", "", 1.0, 100.0, 1.0, false, true, sliderCallback)
    val probabilitySlider = GuiSlider(ID++, 0, 0, 200, 20, probabilityText.unformattedText+": ", "%", 0.0, 100.0, 50.0, true, true, sliderCallback)
    val lavaCheckbox = GuiCheckBox(ID++, 0, 0, lavaText.unformattedText, false)
    val generationTypeButton = GuiButton(ID++, 0, 0, chunkGridText.unformattedText)

    init {
        options.fromJsonString(optionsJson)
        updateView()
    }

    override fun initGui() {
        super.initGui()
        addButton(cancelButton)
        addButton(doneButton)
        addButton(sizeSlider)
        addButton(spacingSlider)
        addButton(lavaCheckbox)

        addButton(probabilitySlider)

        addButton(generationTypeButton)

        generationTypeButton.x = width/2-generationTypeButton.width/2
        generationTypeButton.y = 30

        lavaCheckbox.x = width/2-lavaCheckbox.width/2
        lavaCheckbox.y = 55

        // GRID
        spacingSlider.width = cancelButton.width
        sizeSlider.width = cancelButton.width

        sizeSlider.showDecimal = false
        spacingSlider.showDecimal = false

        sizeSlider.x = width/2 - sizeSlider.width-1
        spacingSlider.x = width/2+1
        sizeSlider.y = 70
        spacingSlider.y = 70

        cancelButton.x = width /2+1
        doneButton.x = width /2 - cancelButton.width-1

        cancelButton.y = height - cancelButton.height - 20
        doneButton.y = cancelButton.y

        // RANDOM
        probabilitySlider.x = width/2-probabilitySlider.width/2
        probabilitySlider.y = 70
    }

    override fun actionPerformed(button: GuiButton) {
        when(button) {
            cancelButton -> mc.displayGuiScreen(parent)
            doneButton -> {
                options.clusterSize = sizeSlider.valueInt
                options.spacing = spacingSlider.valueInt
                options.spawnLava = lavaCheckbox.isChecked
                options.probability = probabilitySlider.value/100.0
                parent.chunkProviderSettingsJson = options.toJsonString()
                mc.displayGuiScreen(parent)
            }

            generationTypeButton -> {
                options.randomChunks = !options.randomChunks
                updateView()
            }
        }
    }

    private fun updateView() {
        generationTypeButton.displayString = if(options.randomChunks) randomChunksText.unformattedText else chunkGridText.unformattedText

        lavaCheckbox.setIsChecked(options.spawnLava)
        sizeSlider.value = options.clusterSize.toDouble()
        spacingSlider.value = options.spacing.toDouble()
        probabilitySlider.value = options.probability*100.0

        sizeSlider.visible = !options.randomChunks
        spacingSlider.visible = !options.randomChunks

        probabilitySlider.visible = options.randomChunks
    }

    override fun updateScreen() {
        super.updateScreen()
        sizeSlider.updateSlider()
        spacingSlider.updateSlider()
        probabilitySlider.updateSlider()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        this.drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        drawCenteredString(fontRenderer, title.unformattedText, width/2, 20, 0xF0F0F0)
    }
}