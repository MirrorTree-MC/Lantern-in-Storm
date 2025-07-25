package top.bearcabbage.lanterninstorm;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import top.bearcabbage.lanterninstorm.lantern.BeginningLanternEntity;
import top.bearcabbage.lanterninstorm.player.LiSPlayer;

import java.util.HashSet;
import java.util.Set;

public class LanternInStormAPI {
    public static boolean setRTPSpawnWhenSpawnpointCommand = true;
    public static final Set<RegistryKey<World>> safeWorlds = new HashSet<>();

    public static void overrideRTPSpawnSetting() {
        setRTPSpawnWhenSpawnpointCommand = false;
    }

    public static void setRTPSpawn(ServerPlayerEntity player, BlockPos pos, boolean warp) {
        if(player instanceof LiSPlayer liSPlayer){
            liSPlayer.getLS().setInvincibleSec(80);
            liSPlayer.getLS().setRtpSpawn(pos);
            if(!setRTPSpawnWhenSpawnpointCommand) player.setSpawnPoint(player.getServer().getOverworld().getRegistryKey(), pos, 0.0F, true, false);
            if(!warp) BeginningLanternEntity.create(player.getServerWorld(), player);
        }
    }

    public static void setWarpSpawn(World world, Position pos, String name) {
        BeginningLanternEntity.createWarp(world, pos, name);
    }

    public static BlockPos getRTPSpawn(ServerPlayerEntity player) {
        return ((LiSPlayer) player).getLS().getOriginalRtpSpawn();
    }

    public static void addSafeWorld(RegistryKey<World> world) {
        safeWorlds.add(world);
    }

}
