package com.feed_the_beast.ftbquests.gui.tree;

import com.feed_the_beast.mods.ftbguilibrary.widget.Panel;
import com.feed_the_beast.mods.ftbguilibrary.widget.WidgetLayout;
import net.minecraftforge.fml.ModList;

/**
 * @author LatvianModder
 */
public class PanelOtherButtonsTop extends PanelOtherButtons
{
	public PanelOtherButtonsTop(Panel panel)
	{
		super(panel);
	}

	@Override
	public void addWidgets()
	{
		int r = treeGui.file.self.getUnclaimedRewards(false);

		if (r > 0)
		{
			add(new ButtonCollectRewards(this, r));
		}

		if (ModList.get().isLoaded("ftbguides"))
		{
			add(new ButtonOpenGuides(this));
		}

		if (!treeGui.file.emergencyItems.isEmpty() && (treeGui.file.self != null || treeGui.file.canEdit()))
		{
			add(new ButtonEmergencyItems(this));
		}

		add(new ButtonWiki(this));
	}

	@Override
	public void alignWidgets()
	{
		setPosAndSize(treeGui.width - width, 1, width, align(WidgetLayout.VERTICAL));
	}
}