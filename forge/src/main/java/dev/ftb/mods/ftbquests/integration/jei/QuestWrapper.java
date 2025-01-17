package dev.ftb.mods.ftbquests.integration.jei;

import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.loot.RewardTable;
import dev.ftb.mods.ftbquests.quest.loot.WeightedReward;
import dev.ftb.mods.ftbquests.quest.reward.RandomReward;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import dev.ftb.mods.ftbquests.quest.task.ItemTask;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.latvian.mods.itemfilters.ItemFilters;
import dev.latvian.mods.itemfilters.api.ItemFiltersAPI;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class QuestWrapper //FIXME: implements IRecipeWrapper
{
	public final Quest quest;
	public final Component name;
	public final List<List<ItemStack>> input;
	public final List<List<ItemStack>> output;

	public QuestWrapper(Quest q, List<Reward> rewards) {
		quest = q;
		name = quest.getTitle();

		input = new ArrayList<>(5);
		output = new ArrayList<>(5);

		if (quest.tasks.size() == 1) {
			input.add(Collections.emptyList());
			input.add(Collections.emptyList());
			input.add(Collections.emptyList());
			input.add(Collections.emptyList());
		}

		for (Task task : quest.tasks) {
			if (task instanceof ItemTask) {
				ItemStack filter = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(ItemFilters.MOD_ID, "or")));
				//FIXME: filter.items.addAll(((ItemTask) task).items);
				input.add(Collections.singletonList(filter));
				continue;
			}

			Object object = task.getIngredient();
			ItemStack stack = object instanceof ItemStack ? (ItemStack) object : ItemStack.EMPTY;

			if (!stack.isEmpty()) {
				List<ItemStack> list = new ArrayList<>();
				ItemFiltersAPI.getDisplayItemStacks(stack, list);
				input.add(list);
			} else if (task.getIcon() instanceof ItemIcon) {
				stack = ((ItemIcon) task.getIcon()).getStack().copy();
				stack.setHoverName(task.getTitle());
				input.add(Collections.singletonList(stack));
			} else {
				stack = new ItemStack(Items.PAINTING);
				stack.setHoverName(task.getTitle());
				stack.addTagElement("icon", StringTag.valueOf(task.getIcon().toString()));
				input.add(Collections.singletonList(stack));
			}
		}

		if (rewards.size() == 1) {
			output.add(Collections.emptyList());
			output.add(Collections.emptyList());
			output.add(Collections.emptyList());
			output.add(Collections.emptyList());
		}

		for (Reward reward : rewards) {
			Object object = reward.getIngredient();
			ItemStack stack = object instanceof ItemStack ? (ItemStack) object : ItemStack.EMPTY;

			if (!stack.isEmpty()) {
				output.add(Collections.singletonList(stack.copy()));
			} else if (reward instanceof RandomReward) {
				List<ItemStack> list = new ArrayList<>();
				RewardTable table = ((RandomReward) reward).getTable();

				if (table.hideTooltip) {
					ItemStack unknown = new ItemStack(Items.BARRIER);
					unknown.setHoverName(new TextComponent("Unknown Reward"));
					list.add(unknown);
				} else {
					for (WeightedReward reward1 : table.rewards) {
						Object object1 = reward1.reward.getIngredient();

						if (object1 instanceof ItemStack) {
							list.add((ItemStack) object1);
						}
					}
				}

				output.add(list);
			} else if (reward.getIcon() instanceof ItemIcon) {
				stack = ((ItemIcon) reward.getIcon()).getStack().copy();
				stack.setHoverName(reward.getTitle());
				output.add(Collections.singletonList(stack));
			} else {
				stack = new ItemStack(Items.PAINTING);
				stack.setHoverName(reward.getTitle());
				stack.addTagElement("icon", StringTag.valueOf(reward.getIcon().toString()));
				output.add(Collections.singletonList(stack));
			}
		}
	}

	/*
	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputLists(VanillaTypes.ITEM, input);
		ingredients.setOutputLists(VanillaTypes.ITEM, output);
	}

	@Override
	public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
		String s = TextFormatting.UNDERLINE + quest.getTitle();
		int w = mc.fontRenderer.getStringWidth(s);
		int h = 9;
		int x = (recipeWidth - w) / 2;
		int y = 3;
		mc.fontRenderer.drawString(s, x, y, (mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h) ? 0xFFA87A5E : 0xFF3F2E23);
	}

	@Override
	public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton)
	{
		if (mouseY >= 0 && mouseY < 20 && !ClientQuestFile.INSTANCE.disableGui)
		{
			ClientQuestFile.INSTANCE.questTreeGui.open(quest, true);
			return true;
		}

		return false;
	}
	 */
}