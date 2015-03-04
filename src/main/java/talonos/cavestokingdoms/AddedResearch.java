package talonos.cavestokingdoms;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import talonos.cavestokingdoms.blocks.CtKBlock;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.wands.WandTriggerRegistry;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.config.ConfigResearch;

public class AddedResearch 
{

	public static void initResearch() 
	{
		//ResearchCategories.registerCategory("ANTITAINT, arg1, arg2);
		
		ShapedArcaneRecipe silverPotRecipe = ThaumcraftApi.addArcaneCraftingRecipe("SILVERPOTION", new ItemStack(CtKItems.silverPotion, 6), new AspectList().add(Aspect.WATER, 15).add(Aspect.ORDER, 24), new Object[] {
				"FLF",
				'L', new ItemStack(ConfigBlocks.blockMagicalLeaves, 1, 1),
				'F', new ItemStack(Items.glass_bottle)
				});
		
		
		ShapedArcaneRecipe purityFocusRecipie = ThaumcraftApi.addArcaneCraftingRecipe("PURITYFOCUS", new ItemStack(CtKItems.purityFocus), new AspectList().add(Aspect.WATER, 5).add(Aspect.ORDER, 8), new Object[] {
			"SQS",
			"QFQ",
			"SQS",
			'S', new ItemStack(ConfigItems.itemShard, 1, 4),
			'Q', new ItemStack(Items.quartz),
			'F', new ItemStack(ConfigBlocks.blockCustomPlant, 1, 4)
			});
		
		/*ShapedArcaneRecipe dawnTotemRecipie = ThaumcraftApi.addArcaneCraftingRecipe("DAWNTOTEM", new ItemStack(CtKBlock.dawnTotem), new AspectList().add(Aspect.EARTH, 60).add(Aspect.ORDER, 60), new Object[] {
			"SSS",
			"SFS",
			"SSS",
			'S', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 7),
			'F', new ItemStack(ConfigBlocks.blockCustomPlant, 1, 4)
			});*/
		
		InfusionRecipe dawnTotemRecipe = ThaumcraftApi.addInfusionCraftingRecipe("DAWNTOTEM", new ItemStack(CtKBlock.dawnTotem), 6, 
				new AspectList().add(Aspect.AURA, 32).add(Aspect.HEAL, 48).add(Aspect.LIFE, 64)
				                .add(Aspect.LIGHT, 32).add(Aspect.ARMOR, 48).add(Aspect.ORDER, 64), 
				new ItemStack(ConfigBlocks.blockMagicalLog,1,1), new ItemStack[] {
			         new ItemStack(ConfigBlocks.blockCustomPlant, 1, 4), new ItemStack(ConfigBlocks.blockCustomPlant, 1, 4), 
			         new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 3), new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 3), 
			         new ItemStack(ConfigBlocks.blockCrystal, 1, 4)});
		
		
		ResearchItem silverPotResearch = new ResearchItem(
				"SILVERPOTION", 
				"ALCHEMY", 
				new AspectList().add(Aspect.WATER, 3).add(Aspect.HEAL, 6).add(Aspect.TAINT, 3).add(Aspect.PLANT, 3), 
				-1, -4, 2,
				new ItemStack(CtKItems.silverPotion));
		silverPotResearch.setPages(new ResearchPage[] 
				{ 
				new ResearchPage("tc.research_page.SILVERPOTION.1"),
				new ResearchPage(silverPotRecipe)});
		silverPotResearch.setConcealed();
		silverPotResearch.setParents(new String[] { "ETHEREALBLOOM" });
		silverPotResearch.registerResearchItem();
		
		ResearchItem purityFocusResearch = new ResearchItem(
				"PURITYFOCUS", 
				"ALCHEMY", 
				new AspectList().add(Aspect.TOOL, 3).add(Aspect.HEAL, 6).add(Aspect.TAINT, 3).add(Aspect.MAGIC, 3), 
				-3, -4, 2, 
				new ItemStack(CtKItems.purityFocus));
		purityFocusResearch.setPages(new ResearchPage[] 
				{ 
				new ResearchPage("tc.research_page.PURITYFOCUS.1"),
				new ResearchPage(purityFocusRecipie)});
		purityFocusResearch.setConcealed();
		purityFocusResearch.setParents(new String[] { "ETHEREALBLOOM" });
		purityFocusResearch.registerResearchItem();
		
		ResearchItem dawnTotemResearch = new ResearchItem(
				"DAWNTOTEM", 
				"ALCHEMY", 
				new AspectList().add(Aspect.AURA, 6).add(Aspect.HEAL, 8).add(Aspect.TAINT, 3).add(Aspect.MAGIC, 6), 
				-2, -6, 2, 
				new ItemStack(CtKBlock.dawnTotem));
		dawnTotemResearch.setPages(new ResearchPage[] 
				{ 
				new ResearchPage("tc.research_page.DAWNTOTEM.1"),
				new ResearchPage(dawnTotemRecipe)});
		dawnTotemResearch.setConcealed();
		dawnTotemResearch.setParents(new String[] { "SILVERPOTION" , "PURITYFOCUS"});
		dawnTotemResearch.registerResearchItem();

		
		WandTriggerRegistry.registerWandBlockTrigger(new TalonosWandTriggerManager(), 0, CtKBlock.dawnTotem, -1, "cavestokingdoms");
	}

}
