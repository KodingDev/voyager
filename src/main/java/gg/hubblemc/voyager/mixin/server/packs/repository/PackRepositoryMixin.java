package gg.hubblemc.voyager.mixin.server.packs.repository;

import gg.hubblemc.voyager.Voyager;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
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
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Mixin(PackRepository.class)
public class PackRepositoryMixin {

    @Shadow
    @Final
    @Mutable
    private Set<RepositorySource> sources;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void construct(RepositorySource[] resourcePackProviders, CallbackInfo info) {
        sources = new HashSet<>(sources);

        String property = System.getProperty("voyager.repo-folders");
        if (property == null) return;

        for (String pathStr : property.split(File.pathSeparator)) {
            try {
                Path path = Path.of(pathStr);
                Voyager.LOGGER.info("Adding pack repository folder: " + path);
                sources.add(new FolderRepositorySource(path, PackType.CLIENT_RESOURCES, PackSource.BUILT_IN));
            } catch (InvalidPathException e) {
                Voyager.LOGGER.error("Invalid path for pack repository folder: " + pathStr);
            }
        }
    }

}
