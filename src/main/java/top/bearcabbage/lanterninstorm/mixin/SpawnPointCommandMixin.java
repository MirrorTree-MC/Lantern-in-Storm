package top.bearcabbage.lanterninstorm.mixin;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SpawnPointCommand;
import net.minecraft.server.command.SummonCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.bearcabbage.lanterninstorm.entity.PrivateLanternEntity;
import top.bearcabbage.lanterninstorm.interfaces.PlayerAccessor;

import java.util.Collection;
import java.util.Iterator;


@Mixin(SpawnPointCommand.class)
public class SpawnPointCommandMixin {

    @Inject(method = "execute", at = @At("TAIL"))
    private static void execute(ServerCommandSource source, Collection<ServerPlayerEntity> targets, BlockPos pos, float angle, CallbackInfoReturnable<Integer> cir) {
        Iterator<ServerPlayerEntity> iterator = targets.iterator();
        while (iterator.hasNext()) {
            ServerPlayerEntity serverPlayerEntity = iterator.next();
            if(serverPlayerEntity instanceof PlayerAccessor playerAccessor){
                playerAccessor.getLS().setInvincibleTick(80);
                playerAccessor.getLS().setRtpSpawn(pos);
                PrivateLanternEntity.create(serverPlayerEntity.getServerWorld(), serverPlayerEntity);
            }
        }
    }
}