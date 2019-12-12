package com.feed_the_beast.ftbquests.net;

import com.feed_the_beast.ftbquests.quest.Quest;
import com.feed_the_beast.ftbquests.quest.ServerQuestFile;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @author LatvianModder
 */
public class MessageMoveQuest extends MessageBase
{
	private final int id;
	private final int chapter;
	private final double x, y;

	MessageMoveQuest(PacketBuffer buffer)
	{
		id = buffer.readInt();
		chapter = buffer.readInt();
		x = buffer.readDouble();
		y = buffer.readDouble();
	}

	public MessageMoveQuest(int i, int c, double _x, double _y)
	{
		id = i;
		chapter = c;
		x = _x;
		y = _y;
	}

	public void write(PacketBuffer buffer)
	{
		buffer.writeInt(id);
		buffer.writeInt(chapter);
		buffer.writeDouble(x);
		buffer.writeDouble(y);
	}

	public void handle(NetworkEvent.Context context)
	{
		Quest quest = ServerQuestFile.INSTANCE.getQuest(id);

		if (quest != null)
		{
			quest.moved(x, y, chapter);
			ServerQuestFile.INSTANCE.save();
			new MessageMoveQuestResponse(id, chapter, x, y).sendToAll();
		}
	}
}