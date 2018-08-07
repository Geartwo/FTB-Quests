package com.feed_the_beast.ftbquests.net;

import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToServer;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbquests.FTBQuests;
import com.feed_the_beast.ftbquests.FTBQuestsItems;
import com.feed_the_beast.ftbquests.block.TileScreen;
import com.feed_the_beast.ftbquests.quest.ServerQuestFile;
import com.feed_the_beast.ftbquests.quest.tasks.QuestTask;
import com.feed_the_beast.ftbquests.util.FTBQuestsTeamData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * @author LatvianModder
 */
public class MessageGetScreen extends MessageToServer
{
	private short task;
	private int size;
	private int type;

	public MessageGetScreen()
	{
	}

	public MessageGetScreen(short t, int s, int ty)
	{
		task = t;
		size = s;
		type = ty;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBQuestsNetHandler.GENERAL;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeShort(task);
		data.writeByte(size);
		data.writeByte(type);
	}

	@Override
	public void readData(DataIn data)
	{
		task = data.readShort();
		size = data.readByte();
		type = data.readByte();
	}

	@Override
	public void onMessage(EntityPlayerMP player)
	{
		if (size >= 0 && size <= 4 && FTBQuests.canEdit(player))
		{
			QuestTask t = ServerQuestFile.INSTANCE.getTask(task);

			if (t != null)
			{
				FTBQuestsTeamData teamData = FTBQuestsTeamData.get(Universe.get().getPlayer(player).team);
				ItemStack stack = new ItemStack(type == 1 ? FTBQuestsItems.FLAT_SCREEN : FTBQuestsItems.SCREEN);
				TileScreen tile = new TileScreen();
				tile.task = t.id;
				tile.owner = teamData.team.getName();
				tile.size = size;
				tile.writeToItem(stack);
				ItemHandlerHelper.giveItemToPlayer(player, stack);
			}
		}
	}
}