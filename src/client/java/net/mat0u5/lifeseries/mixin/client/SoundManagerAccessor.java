package net.mat0u5.lifeseries.mixin.client;

import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = SoundManager.class, priority = 2)
public interface SoundManagerAccessor {

    @Accessor("soundSystem")
    SoundSystem getSoundSystem();
}
