package net.mat0u5.lifeseries.mixin.client;

import com.google.common.collect.Multimap;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = SoundSystem.class, priority = 2)
public interface SoundSystemAccessor {

    @Accessor("sounds")
    Multimap<SoundCategory, SoundInstance> getSounds();
}
