package me.whitehat.EfficientSet

import net.minecraft.server.v1_12_R1.BlockPosition
import net.minecraft.server.v1_12_R1.ChunkSection
import net.minecraft.server.v1_12_R1.PacketPlayOutMapChunk
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlock
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CommandListeners : CommandExecutor{

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        when(sender){
            !is Player  -> return true
            else -> {
                var player : Player = sender
                if(command.name.equals("set")){
                    when (args.size) {
                        1 -> {
                            var changedChunks : ArrayList<Chunk>? = ArrayList()
                            Main.pos1.get(player)?.let { pos1 ->
                                Main.pos2.get(player)?.let { pos2 ->
                                    for(x in min(pos1.blockX, pos2.blockX)..max(pos1.blockX, pos2.blockX)) {
                                        for (z in min(pos1.blockZ, pos2.blockZ)..max(pos1.blockZ, pos2.blockZ)) {
                                            for (y in min(pos1.blockY, pos2.blockY)..max(pos1.blockY, pos2.blockY)) {
                                                var b: Block? = pos1.world?.getBlockAt(
                                                        Location(pos1.world, x.toDouble(), y.toDouble(), z.toDouble()))
                                                setBlockFast(b!!.world, b.location, Material.valueOf(args[0].toUpperCase()))
                                                if(!changedChunks!!.contains(b.chunk))
                                                    changedChunks.add(b.chunk)
                                            }
                                        }
                                    }
                                    for(c in changedChunks!!){
                                        var nmsChunk = (c as CraftChunk).handle
                                        for(p in Bukkit.getOnlinePlayers()){
                                            (p as CraftPlayer).handle.playerConnection.sendPacket(PacketPlayOutMapChunk(nmsChunk, 20))
                                            p.world.refreshChunk(c.x, c.z)
                                        }
                                    }

                                    player.sendMessage(Main.toColor("&aOperation completed successfully."))
                                    return true
                                }
                            }
                            player.sendMessage(Main.toColor("&ePlease select pos1 and pos2."))
                        }
                        else -> {
                            player.sendMessage(Main.toColor("&cUsage: /set [material]"))
                        }
                    }
                }
            }
        }
        return true
    }

    fun setBlockFast(w: World, l: Location,m: Material){
        var nmsWorld = (w as CraftWorld).handle
        var nmsChunk = nmsWorld.getChunkAt(l.blockX shr 4, l.blockZ shr 4)
        var ibd = net.minecraft.server.v1_12_R1.Block.getByName(m.name.toString())?.blockData
        nmsChunk.a(BlockPosition(l.blockX, l.blockY, l.blockZ), ibd)
    }
}