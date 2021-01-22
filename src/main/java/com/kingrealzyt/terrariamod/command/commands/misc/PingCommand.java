package com.kingrealzyt.terrariamod.command.commands.misc;

import com.kingrealzyt.terrariamod.command.CommandContext;
import com.kingrealzyt.terrariamod.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class PingCommand implements ICommand {

    @Override
    public void handle(CommandContext event) {

        Member member = event.getMember();
        TextChannel channel = event.getChannel();
        Member selfBot = member.getGuild().getSelfMember();
        final JDA jda = event.getJDA();

        if (!member.hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
            channel.sendMessage("Sorry " + member.getAsMention() + ", you don't have the perms to do that. (Missing Embed Links Permission)").queue();
            return;
        } else if (!selfBot.hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
            channel.sendMessage("Sorry, I don't have the perms to do that. (I'm Missing Embed Links Permission)").queue();
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();
        long time = System.currentTimeMillis();
        event.getChannel().sendMessage(":signal_strength:Sending.... ").queue(embed -> {
            long latency = System.currentTimeMillis() - time;
            eb.setTitle("Pong! :ping_pong:");
            eb.addField("Latency", latency + "ms", false);
            eb.addField("Discord API", jda.getGatewayPing() + "ms", false);
            eb.setColor(0xd01212);
            channel.sendMessage(eb.build()).queue();
        });

    }

    @Override
    public String getName() {
        return "ping";
    }
}
