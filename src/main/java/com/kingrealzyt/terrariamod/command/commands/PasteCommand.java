package com.kingrealzyt.terrariamod.command.commands;

import com.kingrealzyt.terrariamod.Config;
import com.kingrealzyt.terrariamod.command.CommandContext;
import com.kingrealzyt.terrariamod.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.menudocs.paste.PasteClient;
import org.menudocs.paste.PasteClientBuilder;
import org.menudocs.paste.PasteHost;

import java.time.Instant;
import java.util.List;

public class PasteCommand implements ICommand {


    private final PasteClient client = new PasteClientBuilder()
            .setUserAgent("CBot")
            .setDefaultExpiry("10m")
            .setPasteHost(PasteHost.MENUDOCS)
            .build();

    @Override
    public void handle(CommandContext event) {
        Member member = event.getMember();
        TextChannel channel = event.getChannel();
        Member selfBot = member.getGuild().getSelfMember();

        if (!member.hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
            channel.sendMessage("Sorry " + member.getAsMention() + ", you don't have the perms to do that. (Missing Embed Links Permission)").queue();
            return;
        } else if (!selfBot.hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
            channel.sendMessage("Sorry, I don't have the perms to do that. (I'm Missing Embed Links Permission)").queue();
            return;
        }

        final List<String> args = event.getArgs();

        if (args.size() < 2) {
            channel.sendMessage("Missing arguments | Usage: " + Config.prefix + "paste <language> <what you want to paste>").queue();
            return;
        }

        final String language = args.get(0);
        final String contentRaw = event.getMessage().getContentRaw();
        final int index = contentRaw.indexOf(language) + language.length();
        final String body = contentRaw.substring(index).trim();

        client.createPaste(language, body).async(
                (id) -> client.getPaste(id).async((paste) -> {
                    EmbedBuilder eb = new EmbedBuilder()
                            .setTitle("Paste " + id, paste.getPasteUrl())
                            .setDescription("```")
                            .appendDescription(paste.getLanguage().getId())
                            .appendDescription("\n")
                            .appendDescription(paste.getBody())
                            .appendDescription("```");
                            eb.setFooter("Requested by: " + event.getAuthor().getAsTag());
                            eb.setTimestamp(Instant.now());

                    channel.sendMessage(eb.build()).queue();
                    event.getMessage().delete().queue();
                })
        );
    }

    @Override
    public String getName() {
        return "paste";
    }
}
