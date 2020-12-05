package me.whitehat.EfficientSet

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class Listeners : Listener {

    @EventHandler fun onInteract(e: PlayerInteractEvent){
        if(e.item?.type != Material.STICK)return
        when(e.action){
            Action.RIGHT_CLICK_BLOCK -> {
                e.clickedBlock!!.let {
                    e.player.sendMessage(
                            Main.toColor("&dPos2 set to X: ${it.location.x} Y: ${it.location.y} Z: ${it.location.z}"))
                    Main.pos2.put(e.player, it.location)
                    e.isCancelled = true
                }
            }
            Action.LEFT_CLICK_BLOCK -> {
                e.clickedBlock!!.let {
                    e.player.sendMessage(
                            Main.toColor("&dPos1 set to X: ${it.location.x} Y: ${it.location.y} Z: ${it.location.z}"))
                    Main.pos1.put(e.player, it.location)
                    e.isCancelled = true
                }
            }
            else -> return
        }
    }
}