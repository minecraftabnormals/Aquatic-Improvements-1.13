package com.minecraftabnormals.upgrade_aquatic.core.other;

import com.minecraftabnormals.abnormals_core.core.registry.LootInjectionRegistry;
import com.minecraftabnormals.abnormals_core.core.util.DataUtil;
import com.minecraftabnormals.upgrade_aquatic.core.UpgradeAquatic;
import com.minecraftabnormals.upgrade_aquatic.core.registry.UABlocks;
import com.minecraftabnormals.upgrade_aquatic.core.registry.UAItems;
import net.minecraft.loot.LootTables;

public class UACompat {

	public static void registerCompat() {
		registerLootInjectors();
		registerCompostables();
		registerFlammables();
	}

	public static void registerLootInjectors() {
		LootInjectionRegistry.LootInjector injector = new LootInjectionRegistry.LootInjector(UpgradeAquatic.MOD_ID);
		injector.addLootInjection(injector.buildLootPool("tooth_ruins", 1, 0), LootTables.CHESTS_UNDERWATER_RUIN_BIG);
		injector.addLootInjection(injector.buildLootPool("tooth_treasure", 1, 0), LootTables.CHESTS_BURIED_TREASURE);
		injector.addLootInjection(injector.buildLootPool("pickerelweed_structures", 1, 0), LootTables.CHESTS_SHIPWRECK_SUPPLY);
	}

	public static void registerCompostables() {
		DataUtil.registerCompostable(UABlocks.RIVER_LEAVES.get(), 0.30F);
		DataUtil.registerCompostable(UABlocks.RIVER_SAPLING.get(), 0.30F);
		DataUtil.registerCompostable(UABlocks.RIVER_LEAF_CARPET.get(), 0.30F);
		DataUtil.registerCompostable(UAItems.MULBERRY.get(), 0.30F);
		DataUtil.registerCompostable(UAItems.MULBERRY_BREAD.get(), 0.85F);
		DataUtil.registerCompostable(UAItems.MULBERRY_PIE.get(), 1.0F);
		DataUtil.registerCompostable(UABlocks.MULBERRY_PUNNET.get(), 1.0F);
		DataUtil.registerCompostable(UABlocks.MULBERRY_JAM_BLOCK.get(), 1.0F);

		DataUtil.registerCompostable(UABlocks.BEACHGRASS.get(), 0.30F);
		DataUtil.registerCompostable(UABlocks.TALL_BEACHGRASS.get(), 0.65F);
		DataUtil.registerCompostable(UABlocks.BEACHGRASS_THATCH.get(), 0.65F);
		DataUtil.registerCompostable(UABlocks.BEACHGRASS_THATCH_STAIRS.get(), 0.65F);
		DataUtil.registerCompostable(UABlocks.BEACHGRASS_THATCH_SLAB.get(), 0.65F);
		DataUtil.registerCompostable(UABlocks.BEACHGRASS_THATCH_VERTICAL_SLAB.get(), 0.65F);

		DataUtil.registerCompostable(UABlocks.BLUE_PICKERELWEED.get(), 0.30F);
		DataUtil.registerCompostable(UAItems.BOILED_BLUE_PICKERELWEED.get(), 0.30F);
		DataUtil.registerCompostable(UABlocks.BLUE_PICKERELWEED_BLOCK.get(), 0.50F);
		DataUtil.registerCompostable(UABlocks.BOILED_BLUE_PICKERELWEED_BLOCK.get(), 0.50F);

		DataUtil.registerCompostable(UABlocks.PURPLE_PICKERELWEED.get(), 0.30F);
		DataUtil.registerCompostable(UAItems.BOILED_PURPLE_PICKERELWEED.get(), 0.30F);
		DataUtil.registerCompostable(UABlocks.PURPLE_PICKERELWEED_BLOCK.get(), 0.50F);
		DataUtil.registerCompostable(UABlocks.BOILED_PURPLE_PICKERELWEED_BLOCK.get(), 0.50F);

		DataUtil.registerCompostable(UABlocks.FLOWERING_RUSH.get(), 0.65F);
		DataUtil.registerCompostable(UABlocks.WHITE_SEAROCKET.get(), 0.65F);
		DataUtil.registerCompostable(UABlocks.PINK_SEAROCKET.get(), 0.65F);

		DataUtil.registerCompostable(UABlocks.TONGUE_KELP.get(), 0.30F);
		DataUtil.registerCompostable(UABlocks.THORNY_KELP.get(), 0.30F);
		DataUtil.registerCompostable(UABlocks.OCHRE_KELP.get(), 0.30F);
		DataUtil.registerCompostable(UABlocks.POLAR_KELP.get(), 0.30F);

		DataUtil.registerCompostable(UABlocks.KELP_BLOCK.get(), 0.50F);
		DataUtil.registerCompostable(UABlocks.TONGUE_KELP_BLOCK.get(), 0.50F);
		DataUtil.registerCompostable(UABlocks.THORNY_KELP_BLOCK.get(), 0.50F);
		DataUtil.registerCompostable(UABlocks.OCHRE_KELP_BLOCK.get(), 0.50F);
		DataUtil.registerCompostable(UABlocks.POLAR_KELP_BLOCK.get(), 0.50F);
	}

	public static void registerFlammables() {
		DataUtil.registerFlammable(UABlocks.MULBERRY_VINE.get(), 60, 100);
		
		DataUtil.registerFlammable(UABlocks.BLUE_PICKERELWEED_BLOCK.get(), 30, 60);
		DataUtil.registerFlammable(UABlocks.PURPLE_PICKERELWEED_BLOCK.get(), 30, 60);
		DataUtil.registerFlammable(UABlocks.BOILED_BLUE_PICKERELWEED_BLOCK.get(), 30, 60);
		DataUtil.registerFlammable(UABlocks.BOILED_PURPLE_PICKERELWEED_BLOCK.get(), 30, 60);
		DataUtil.registerFlammable(UABlocks.BLUE_PICKERELWEED.get(), 60, 100);
		DataUtil.registerFlammable(UABlocks.PURPLE_PICKERELWEED.get(), 60, 100);
		DataUtil.registerFlammable(UABlocks.TALL_BLUE_PICKERELWEED.get(), 60, 100);
		DataUtil.registerFlammable(UABlocks.TALL_PURPLE_PICKERELWEED.get(), 60, 100);

		DataUtil.registerFlammable(UABlocks.BEACHGRASS.get(), 60, 20);
		DataUtil.registerFlammable(UABlocks.TALL_BEACHGRASS.get(), 60, 20);
		DataUtil.registerFlammable(UABlocks.BEACHGRASS_THATCH.get(), 60, 20);
		DataUtil.registerFlammable(UABlocks.BEACHGRASS_THATCH_STAIRS.get(), 60, 20);
		DataUtil.registerFlammable(UABlocks.BEACHGRASS_THATCH_SLAB.get(), 60, 20);
		DataUtil.registerFlammable(UABlocks.BEACHGRASS_THATCH_VERTICAL_SLAB.get(), 60, 20);
	    
		DataUtil.registerFlammable(UABlocks.DRIFTWOOD_LOG.get(), 5, 5);
		DataUtil.registerFlammable(UABlocks.DRIFTWOOD.get(), 5, 5);
		DataUtil.registerFlammable(UABlocks.STRIPPED_DRIFTWOOD_LOG.get(), 5, 5);
		DataUtil.registerFlammable(UABlocks.STRIPPED_DRIFTWOOD.get(), 5, 5);
		DataUtil.registerFlammable(UABlocks.DRIFTWOOD_PLANKS.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.DRIFTWOOD_SLAB.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.DRIFTWOOD_STAIRS.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.DRIFTWOOD_FENCE.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.DRIFTWOOD_FENCE_GATE.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.VERTICAL_DRIFTWOOD_PLANKS.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.DRIFTWOOD_VERTICAL_SLAB.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.DRIFTWOOD_BOOKSHELF.get(), 30, 20);
		DataUtil.registerFlammable(UABlocks.DRIFTWOOD_POST.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.STRIPPED_DRIFTWOOD_POST.get(), 5, 20);
        
		DataUtil.registerFlammable(UABlocks.RIVER_LEAVES.get(), 30, 60);
		DataUtil.registerFlammable(UABlocks.RIVER_LOG.get(), 5, 5);
		DataUtil.registerFlammable(UABlocks.RIVER_WOOD.get(), 5, 5);
		DataUtil.registerFlammable(UABlocks.STRIPPED_RIVER_LOG.get(), 5, 5);
		DataUtil.registerFlammable(UABlocks.STRIPPED_RIVER_WOOD.get(), 5, 5);
		DataUtil.registerFlammable(UABlocks.RIVER_PLANKS.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.RIVER_SLAB.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.RIVER_STAIRS.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.RIVER_FENCE.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.RIVER_FENCE_GATE.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.VERTICAL_RIVER_PLANKS.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.RIVER_LEAF_CARPET.get(), 30, 60);
		DataUtil.registerFlammable(UABlocks.RIVER_VERTICAL_SLAB.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.RIVER_BOOKSHELF.get(), 30, 20);
		DataUtil.registerFlammable(UABlocks.RIVER_POST.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.STRIPPED_RIVER_POST.get(), 5, 20);
		DataUtil.registerFlammable(UABlocks.RIVER_HEDGE.get(), 5, 20);
	}
}