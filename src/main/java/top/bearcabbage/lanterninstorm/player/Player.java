package top.bearcabbage.lanterninstorm.player;

import eu.pb4.playerdata.api.PlayerDataApi;
import net.minecraft.advancement.Advancement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import top.bearcabbage.lanterninstorm.LanternInStorm;
import top.bearcabbage.lanterninstorm.LanternInStormSpiritManager;
import top.bearcabbage.lanterninstorm.entity.SpiritLanternEntity;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import static top.bearcabbage.lanterninstorm.utils.Math.HorizontalDistance;

public class Player {
    private ServerPlayerEntity player;
    private BlockPos rtpSpawn;
    private int spiritsTotal;
    private int spiritsBanlance;
    private static final int INIT_SPIRIT = 5;
//    private Team team;
//    private boolean isTeamed;


    private final ReentrantLock lock = new ReentrantLock();

    private static final int TICK_INTERVAL = 20;
    private static final int GRACE_TICK = 100;
    private static final int DAMAGE_INTERVAL = 10;
    private static final float DAMAGE = 2.0F;
    private int LSTick;
    private int tiredTick;
    private int debuffTick;

    public Player(ServerPlayerEntity player) {
        this.player = player;
        NbtCompound data = PlayerDataApi.getCustomDataFor(player, LanternInStorm.LSData);
        if(data == null){
            spiritsTotal = INIT_SPIRIT;
            spiritsBanlance = spiritsTotal;
            data = new NbtCompound();
            data.putInt("spiritsTotal", INIT_SPIRIT);
            data.putInt("spiritsBanlance", INIT_SPIRIT);
            data.putIntArray("rtpspawn", new int[]{-1});
            PlayerDataApi.setCustomDataFor(player, LanternInStorm.LSData, data);
        }
        int[] posVec = data.getIntArray("rtpspawn");
        if (posVec.length == 3) {
            this.rtpSpawn = new BlockPos(posVec[0], posVec[1], posVec[2]);
        }
        spiritsTotal = data.getInt("spiritTotal");
        spiritsBanlance = data.getInt("spiritBanlance");
        LSTick = 0;
        tiredTick = 0;
        spiritsBanlance = 100;
        System.out.println(this.spiritsBanlance);
    }

    public boolean onTick() {
        return ++LSTick % TICK_INTERVAL == 0;
    }

    public void onTiredTick() {
//        this.player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 3*TICK_INTERVAL, 0, false, true));
//        if(++tiredTick >GRACE_TICK){
//            if(debuffTick++%DAMAGE_INTERVAL==0){
//                this.player.damage(player.getDamageSources().genericKill(),DAMAGE);
//            }
//        }
    }

    public void onRestTick() {
        if(--tiredTick ==0){
            this.player.removeStatusEffect(StatusEffects.BLINDNESS);
            debuffTick = 0;
        }
    }

    public void onUnstableTick() {}

    public void onDeath() {
        LSTick = debuffTick = tiredTick = 0;
    }


    public int getTiredTick() {
        return tiredTick;
    }

    public int getSpiritsTotal() {
        return this.spiritsTotal;
    }

    public int getSpiritsBanlance(){
        return this.spiritsBanlance;
    }

    public void onGrantAdvancement(Advancement advancement) {
        this.spiritsTotal++;
        this.spiritsBanlance++;
    }

    public boolean isSafe(){
        return LanternInStormSpiritManager.playerIsSafe(this.player);
    }

    public BlockPos getRtpSpawn() {
        if(this.rtpSpawn == null) {
            return player.getSpawnPointPosition();
        }
        return this.rtpSpawn;
    }

    public boolean setRtpSpawn(BlockPos pos) {
        if(rtpSpawn != null){
            return false;
        }
        this.rtpSpawn = pos;
        NbtCompound data = new NbtCompound();
        data.putIntArray("rtpspawn", new int[]{pos.getX(), pos.getY(), pos.getZ()});
        data.putIntArray("spawnpoint", new int[]{pos.getX(), pos.getY(), pos.getZ()});
        PlayerDataApi.setCustomDataFor(player, LanternInStorm.LSData, data);
        return true;
    }

    public ActionResult distributeSpirits(SpiritLanternEntity entity, int spirits) {
        if(spirits > spiritsBanlance){
            this.player.sendMessage(Text.of("灵魂不够了～～～"));
            return ActionResult.SUCCESS;
        }
        if(LanternInStormSpiritManager.playerDistributeSpirits(player, entity, spirits)){
            spiritsBanlance -= spirits;
            this.player.sendMessage(Text.of("成功分配了"+spirits+"个灵魂"));
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

//    public boolean isTeamed() {
//        return isTeamed;
//    }

//    public boolean joinTeam(Team newTeam) {
//        lock.lock();
//        try {
//            if(this.isTeamed || newTeam == null) {
//                return false;
//            }
//            isTeamed = true;
//            this.team = newTeam;
//            return true;
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public boolean quitTeam() {
//        lock.lock();
//        try {
//            if(!this.isTeamed) {
//                return false;
//            }
//            isTeamed = false;
//            this.team = null;
//            return true;
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public Team getTeam(){
//        return this.team;
//    }



}
