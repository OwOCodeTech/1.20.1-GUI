package net.prizowo.examplemod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.prizowo.examplemod.Examplemod;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Examplemod.MOD_ID);

    public static final RegistryObject<Block> MODE_SWITCHER = BLOCKS.register("mode_switcher",
            () -> new ModeSwitcherBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(2.0f)
                    .requiresCorrectToolForDrops()));
} 