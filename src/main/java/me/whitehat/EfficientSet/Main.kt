package me.whitehat.EfficientSet

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    companion object {
        var instance : Main? = null
        var pos1 = hashMapOf<Player, Location>()
        var pos2 = hashMapOf<Player, Location>()
        fun toColor(s: String): String {
            return ChatColor.translateAlternateColorCodes('&', s)
        }
    }

    override fun onEnable() {
        instance = this
        Bukkit.getConsoleSender().sendMessage(toColor("&aEfficientSet enabled! (Made by WhitehatD)"))
        Bukkit.getPluginManager().registerEvents(Listeners(), instance!!)
        getCommand("set")?.setExecutor(CommandListeners())
    }


}