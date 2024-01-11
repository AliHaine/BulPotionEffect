package com.alihaine.bulcommandeffect.utils;

import com.alihaine.bulcommandeffect.BULCommandEffect
import com.alihaine.bulcommandeffect.core.CommandEffect
import com.alihaine.bulcommandeffect.core.Effect
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.potion.PotionEffectType

class Config {
    companion object {
        private val bulCommandEffect: BULCommandEffect = BULCommandEffect.bulCommandEffect
        private var config: FileConfiguration = bulCommandEffect.config
        private var effectLists: MutableList<CommandEffect> = setupEffectLists()

        fun getConfigString(path: String): String? {
            return config.getString(path)
        }

        fun getConfigStringList(path: String): MutableList<String?> {
            return config.getStringList(path)
        }

        fun getConfigBoolean(path: String): Boolean {
            return config.getBoolean(path)
        }

        fun getConfigInt(path: String): Int {
            return config.getInt(path)
        }

        fun reloadConfig() {
            bulCommandEffect.reloadConfig()
            config = bulCommandEffect.config
            effectLists = setupEffectLists()
        }

        private fun setupEffectLists(): MutableList<CommandEffect> {
            val section: ConfigurationSection = config.getConfigurationSection("effects") ?: return mutableListOf()
            val list: MutableList<CommandEffect> = mutableListOf()

            for (key in section.getKeys(false)) {
                val commandsList: MutableList<String?> = getConfigStringList("${section.name}.$key.commands")
                val effectsList: MutableList<Effect> = convertStringListToEffect(getConfigStringList("${section.name}.$key.effects"))
                var duration: Int = getConfigInt("${section.name}.$key.duration") * 20
                val perm: String? = getConfigString("${section.name}.$key.perm")

                if (duration == 0)
                    duration = 99999;
                list.add(CommandEffect(commandsList, effectsList, duration, perm))
            }
            return list;
        }

        private fun convertStringListToEffect(stringList: MutableList<String?>): MutableList<Effect> {
            val effectsList: MutableList<Effect> = mutableListOf()
            var amplifier: Int

            for (str in stringList) {
                if (str == null)
                    continue;
                amplifier = str.last().digitToInt() - 1;

                //todo check non null
                val po: PotionEffectType? = PotionEffectType.getByName(str.dropLast(2))
                effectsList.add(Effect(po, amplifier))

            }
            return effectsList
        }

        fun getCommandEffectFromCommand(command: String): CommandEffect? {
            return null
        }
        fun test(): MutableList<CommandEffect> {
            return effectLists
        }
    }
}
