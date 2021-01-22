package com.kingrealzyt.terrariamod;

import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.List;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} is ready!", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String prefix = Config.prefix;
        String raw = event.getMessage().getContentRaw();

        if (raw.equalsIgnoreCase(prefix + "shutdown")
                && event.getAuthor().getId().equals(Config.owner_id)) {
            LOGGER.info("Shutting down");
            event.getMessage().getChannel().sendMessage("I am shutting down!").queue();
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());
        }

        if (raw.startsWith(prefix)) {
            manager.handle(event, prefix);
        }

        String msg = event.getMessage().getContentRaw().toLowerCase();

        if (msg.contains("<@!801838812787376138>")) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(0xd01212);
            eb.setTitle("TMBot Info!");
            eb.setFooter("Requested by: " + event.getMessage().getAuthor().getAsTag());
            eb.setTimestamp(Instant.now());
            eb.addField("Version", "1.1A", true);
            eb.addField("License", "[GNU General Public License v3.0](https://www.gnu.org/licenses/gpl-3.0.en.html)", true);
            eb.addField("Current Prefix: ", "`" + Config.prefix + "`", true);
            eb.addField("API", "[JDA](https://github.com/DV8FromTheWorld/JDA)", true);

            event.getChannel().sendMessage(eb.build()).queue();
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        final List<TextChannel> textChannel = event.getGuild().getTextChannelsByName("welcome", true);
        EmbedBuilder eb = new EmbedBuilder();

        if (textChannel.isEmpty()) {
            return;
        }

        final TextChannel yestextChannel = textChannel.get(0);

        eb.setTitle("Welcome!");
        eb.addField("Welcome to the server", event.getMember().getUser().getAsMention() + " to " + event.getGuild().getName(), true);
        eb.setThumbnail(event.getMember().getUser().getEffectiveAvatarUrl());
        eb.setTimestamp(Instant.now());
        eb.setColor(0xd01212);

        yestextChannel.sendMessage(eb.build()).queue();
    }

    @Override
    public void onGuildMemberLeave(@NotNull GuildMemberLeaveEvent event) {
        final List<TextChannel> textChannel = event.getGuild().getTextChannelsByName("welcome", true);
        EmbedBuilder eb = new EmbedBuilder();

        if (textChannel.isEmpty()) {
            return;
        }

        final TextChannel yestextChannel = textChannel.get(0);

        eb.setTitle("Goodbye!");
        eb.addField(event.getMember().getUser().getAsTag(), " left " + event.getGuild().getName() + " this is very sad :(", true);
        eb.setThumbnail(event.getMember().getUser().getEffectiveAvatarUrl());
        eb.setTimestamp(Instant.now());
        eb.setColor(0xd01212);

        yestextChannel.sendMessage(eb.build()).queue();
    }
}
