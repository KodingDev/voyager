package gg.hubblemc.voyager.mixin.server.packs.repository;

import gg.hubblemc.voyager.Voyager;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@Mixin(PackRepository.class)
public class PackRepositoryMixin {

    @Shadow
    @Final
    @Mutable
    private Set<RepositorySource> sources;

    @Inject(method = "<init>(Lnet/minecraft/server/packs/repository/Pack$PackConstructor;[Lnet/minecraft/server/packs/repository/RepositorySource;)V", at = @At("RETURN"))
    public void construct(Pack.PackConstructor arg, RepositorySource[] resourcePackProviders, CallbackInfo info) {
        sources = new HashSet<>(sources);

        String property = System.getProperty("voyager.repo-folders");
        if (property == null) return;

        for (String path : property.split(";")) {
            Voyager.LOGGER.info("Adding pack repository folder: " + path);
            sources.add(new FolderRepositorySource(new File(path), PackSource.BUILT_IN));
        }
    }

}
