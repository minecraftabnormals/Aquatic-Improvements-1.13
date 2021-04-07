package com.minecraftabnormals.upgrade_aquatic.common.entities.pike;

import com.google.common.collect.Lists;
import net.minecraft.util.Util;
import net.minecraft.util.WeightedList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * @author SmellyModder (Luke Tonon)
 */
public enum PikeType {
	AMUR(1, PikeSize.SMALL, PikeRarity.UNCOMMON, null, Biome.Category.SWAMP),
	REDFIN(2, PikeSize.SMALL, PikeRarity.COMMON, null, null),
	BROWN_NORTHERN(3, PikeSize.LARGE, PikeRarity.COMMON, 8, null),
	MAHOGANY_NORTHERN(4, PikeSize.LARGE, PikeRarity.UNCOMMON, 9, null),
	JADE_NORTHERN(5, PikeSize.LARGE, PikeRarity.RARE, 10, null),
	OLIVE_NORTHERN(6, PikeSize.LARGE, PikeRarity.SUPER_RARE, 11, null),
	SPECTRAL(7, PikeSize.LARGE, PikeRarity.LEGENDARY, null, null),
	SPOTTED_BROWN_NORTHERN(8, PikeSize.LARGE, PikeRarity.COMMON, null, null),
	SPOTTED_MAHOGANY_NORTHERN(9, PikeSize.LARGE, PikeRarity.UNCOMMON, null, null),
	SPOTTED_JADE_NORTHERN(10, PikeSize.LARGE, PikeRarity.RARE, null, null),
	SPOTTED_OLIVE_NORTHERN(11, PikeSize.LARGE, PikeRarity.SUPER_RARE, null, null),
	SUPERCHARGED(12, PikeSize.LARGE, PikeRarity.LEGENDARY, null, null),
	OBSIDIAN(13, PikeSize.LARGE, PikeRarity.LEGENDARY, null, null),
	MUSKELLUNGE(14, PikeSize.HUGE, PikeRarity.SUPER_RARE, null, Biome.Category.RIVER),
	CHAIN_PICKEREL(15, PikeSize.SMALL, PikeRarity.COMMON, null, Biome.Category.RIVER),
	GRASS_PICKEREL(16, PikeSize.SMALL, PikeRarity.COMMON, null, Biome.Category.SWAMP),
	BLACK_SOUTHERN(17, PikeSize.MEDIUM, PikeRarity.COMMON, null, null),
	EBONY_SOUTHERN(18, PikeSize.MEDIUM, PikeRarity.UNCOMMON, null, null),
	MUSTARD_SOUTHERN(19, PikeSize.MEDIUM, PikeRarity.RARE, null, null),
	LEMON_SOUTHERN(20, PikeSize.MEDIUM, PikeRarity.RARE, null, null),
	GOLDEN_SOUTHERN(21, PikeSize.MEDIUM, PikeRarity.SUPER_RARE, null, null);
	
	public final int id;
	public final PikeSize pikeSize;
	public final PikeRarity rarity;
	@Nullable
	private final Integer spottedVariant;
	@Nullable
	private final Biome.Category biomeCategory;
	
	private PikeType(int id, PikeSize pikeSize, PikeRarity rarity, @Nullable Integer spottedVariant, @Nullable Biome.Category biomeCategory) {
		this.id = id;
		this.pikeSize = pikeSize;
		this.rarity = rarity;
		this.spottedVariant = spottedVariant;
		this.biomeCategory = biomeCategory;
	}
	
	public static PikeType getTypeById(int id) {
		for (PikeType type : values()) {
			if (type.id == id) return type;
		}
		return PikeType.AMUR;
	}

	public static PikeType getRandom(Random rand, Biome.Category category, boolean fromBucket) {
		WeightedList<List<PikeType>> possibleRarityTypes = Util.make(new WeightedList<>(), (list) -> {
			for (PikeRarity rarity : PikeRarity.values()) list.func_226313_a_(PikeType.getTypeList(category, rarity, fromBucket), rarity.weight);
		});
		List<PikeType> possibleTypes = possibleRarityTypes.func_226318_b_(rand);
		PikeType type = possibleTypes.get(rand.nextInt(possibleTypes.size()));
		Integer spottedVariant = type.spottedVariant;
		return spottedVariant != null && rand.nextFloat() < 0.2F ? PikeType.getTypeById(spottedVariant) : type;
	}
	
	private static List<PikeType> getTypeList(Biome.Category category, PikeRarity rarity, boolean fromBucket) {
		List<PikeType> pikeTypes = Lists.newArrayList();
		List<PikeType> spotted = Lists.newArrayList();
		for (PikeType type : PikeType.values()) {
			if ((fromBucket || type.biomeCategory == null || type.biomeCategory == category) && type.rarity == rarity && !spotted.contains(type)) {
				Integer spottedVariant = type.spottedVariant;
				if (spottedVariant != null) {
					spotted.add(PikeType.getTypeById(spottedVariant));
				}
				pikeTypes.add(type);
			}
		}
		return pikeTypes;
	}
	
	public enum PikeSize {
		SMALL(1.2F, 1.4F),
		MEDIUM(1.5F, 1.7F),
		LARGE(1.7F, 1.9F),
		HUGE(2.3F, 2.5F);
		
		public final float renderSize;
		public final float boxSize;
		
		private PikeSize(float renderSize, float boxSize) {
			this.renderSize = renderSize;
			this.boxSize = boxSize;
		}
	}
	
	public enum PikeRarity {
		COMMON(TextFormatting.GRAY, 55),
		UNCOMMON(TextFormatting.GREEN, 25),
		RARE(TextFormatting.BLUE, 15),
		SUPER_RARE(TextFormatting.GOLD, 5),
		LEGENDARY(TextFormatting.LIGHT_PURPLE, 1);
		
		public final TextFormatting formatting;
		private final int weight;
		
		private PikeRarity(TextFormatting formatting, int weight) {
			this.formatting = formatting;
			this.weight = weight;
		}
	}
}