package net.mat0u5.lifeseries.seasons.boogeyman.advanceddeaths.deaths;

import net.mat0u5.lifeseries.seasons.boogeyman.advanceddeaths.AdvancedDeath;
import net.mat0u5.lifeseries.utils.other.OtherUtils;
import net.mat0u5.lifeseries.utils.player.PlayerUtils;
import net.mat0u5.lifeseries.utils.world.WorldUtils;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class DeathLightning extends AdvancedDeath {
    private Random rnd = new Random();
    private ServerWorld world;
    public DeathLightning(ServerPlayerEntity player) {
        super(player);
    }

    @Override
    protected int maxTime() {
        return 200;
    }

    @Override
    protected DamageSource damageSource(ServerPlayerEntity player) {
        return player.getDamageSources().lightningBolt();
    }

    @Override
    protected void tick(ServerPlayerEntity player) {
        ServerWorld world = PlayerUtils.getServerWorld(player);
        if (ticks > 160) {
            WorldUtils.summonHarmlessLightning(player);
            PlayerUtils.damage(player, player.getDamageSources().lightningBolt(), 100000f);
        }
        else if (ticks > 80) {
            int distanceFromTarget = rnd.nextInt(15, 100);
            Vec3d offset = new Vec3d(
                    world.random.nextDouble() * 2 - 1,
                    0,
                    world.random.nextDouble() * 2 - 1
            ).normalize().multiply(distanceFromTarget);

            Vec3d pos = player.getPos().add(offset.getX(), 0, offset.getZ());
            Vec3d lightningPos = new Vec3d(pos.x, WorldUtils.findTopSafeY(world, pos), pos.z);
            WorldUtils.summonHarmlessLightning(world, lightningPos);
        }
    }

    @Override
    protected void begin(ServerPlayerEntity player) {
        world = PlayerUtils.getServerWorld(player);
        world.setWeather(0, 180, true, true);
    }

    @Override
    protected void end() {
        if (world == null) return;
        world.setWeather(12000, 0, false, false);
    }
}
