package com.feed_the_beast.ftbquests.gui.tree;

import com.feed_the_beast.ftbquests.quest.Chapter;
import com.feed_the_beast.ftbquests.quest.theme.property.ThemeProperties;
import com.feed_the_beast.mods.ftbguilibrary.icon.Color4I;
import com.feed_the_beast.mods.ftbguilibrary.utils.MouseButton;
import com.feed_the_beast.mods.ftbguilibrary.widget.Panel;
import com.feed_the_beast.mods.ftbguilibrary.widget.Theme;
import com.mojang.blaze3d.platform.GlStateManager;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ButtonChapter extends ButtonTab
{
	public Chapter chapter;

	public ButtonChapter(Panel panel, Chapter c)
	{
		super(panel, c.getTitle(), c.getIcon());
		chapter = c;
	}

	@Override
	public void onClicked(MouseButton button)
	{
	}

	@Override
	public void addMouseOverText(List<String> list)
	{
	}

	@Override
	public boolean checkMouseOver(int mouseX, int mouseY)
	{
		if (treeGui.viewQuestPanel.isMouseOver())
		{
			return false;
		}

		return super.checkMouseOver(mouseX, mouseY);
	}

	@Override
	public void draw(Theme theme, int x, int y, int w, int h)
	{
		if (chapter == treeGui.selectedChapter || treeGui.selectedChapter != null && chapter == treeGui.selectedChapter.group)
		{
			Color4I backgroundColor = ThemeProperties.WIDGET_BACKGROUND.get(treeGui.selectedChapter);
			backgroundColor.draw(x + 1, y, w - 2, h);
		}

		if (treeGui.chapterHoverPanel.chapter == this)
		{
			return;
		}

		int is = width < 18 ? 8 : 16;
		icon.draw(x + (w - is) / 2, y + (h - is) / 2, is, is);

		if (chapter.quests.isEmpty() && !chapter.hasChildren())
		{
			GlStateManager.pushMatrix();
			GlStateManager.translatef(0, 0, 450);
			ThemeProperties.CLOSE_ICON.get().draw(x + w - 10, y + 2, 8, 8);
			GlStateManager.popMatrix();
			return;
		}

		if (treeGui.file.self.hasUnclaimedRewards(chapter, true))
		{
			GlStateManager.pushMatrix();
			GlStateManager.translatef(0, 0, 450);
			ThemeProperties.ALERT_ICON.get().draw(x + w - 7, y + 2, 6, 6);
			GlStateManager.popMatrix();
		}
		else if (treeGui.file.self.isComplete(chapter))
		{
			GlStateManager.pushMatrix();
			GlStateManager.translatef(0, 0, 450);
			ThemeProperties.CHECK_ICON.get().draw(x + w - 8, y + 1, 8, 8);
			GlStateManager.popMatrix();
		}
	}

	@Nullable
	@Override
	public Object getIngredientUnderMouse()
	{
		return icon.getIngredient();
	}
}