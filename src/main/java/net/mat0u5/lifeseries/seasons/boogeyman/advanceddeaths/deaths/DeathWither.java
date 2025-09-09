package net.mat0u5.lifeseries.seasons.boogeyman.advanceddeaths.deaths;

import net.mat0u5.lifeseries.seasons.boogeyman.advanceddeaths.AdvancedDeath;
import net.mat0u5.lifeseries.seasons.boogeyman.advanceddeaths.AdvancedDeaths;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class DeathWither extends AdvancedDeath {
    public DeathWither(ServerPlayerEntity player) {
        super(player);
    }

    @Override
    public AdvancedDeaths getDeathType() {
        return AdvancedDeaths.WITHER;
    }

    @Override
    protected int maxTime() {
        return 400;
    }

    @Override
    protected DamageSource damageSource(ServerPlayerEntity player) {
        return player.getDamageSources().wither();
    }

    @Override
    protected void tick(ServerPlayerEntity player) {
        StatusEffectInstance witherEffect = new StatusEffectInstance(StatusEffects.WITHER, -1, 2, false, false, false);
        player.addStatusEffect(witherEffect);
        if (player.hurtTime == 10 && ticks < 80) {
            player.playSoundToPlayer(SoundEvents.ENTITY_WITHER_SHOOT, SoundCategory.PLAYERS, 1, 1);
        }
    }

    @Override
    protected void begin(ServerPlayerEntity player) {
    }

    @Override
    protected void end() {
        if (playerNotFound()) return;
        ServerPlayerEntity player = getPlayer();
        player.removeStatusEffect(StatusEffects.WITHER);
    }
}
