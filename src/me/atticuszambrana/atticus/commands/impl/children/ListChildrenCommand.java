package me.atticuszambrana.atticus.commands.impl.children;

import java.awt.Color;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import me.atticuszambrana.atticus.commands.Command;
import me.atticuszambrana.atticus.manager.PluginManager;
import me.atticuszambrana.atticus.permissions.Rank;
import me.atticuszambrana.atticus.relationships.Relationships;
import me.atticuszambrana.atticus.relationships.children.Child;
import me.atticuszambrana.atticus.util.MessageUtil;

public class ListChildrenCommand extends Command {
	
	public ListChildrenCommand() {
		super("listchildren", "See your current children", Rank.NONE);
	}

	@Override
	public void execute(String[] args, MessageCreateEvent event) {
		// First get the children of this person
		User author = event.getMessageAuthor().asUser().get();
		
		Relationships rel = (Relationships) PluginManager.getPlugin(5);
		
		try {
			
			if(!rel.hasChildren(author, event.getServer().get())) {
				event.getChannel().sendMessage(MessageUtil.message(Color.RED, "You don't have any kids!", "It looks like you do not have any children!"));
				return;
			}
			
			List<Child> cList = rel.getChildren(author, event.getServer().get());
			
			for(Child c : cList) {
				EmbedBuilder em = new EmbedBuilder();
				em.setColor(c.getGender().getColor());
				em.setTitle(c.getName());
				em.addInlineField("Name", c.getName());
				em.addInlineField("ID", String.valueOf(c.getID()));
				em.addInlineField("Gender", c.getGender().getName());
				
				LocalDate date = new Timestamp(c.getBirthstamp()).toLocalDateTime().toLocalDate();
				LocalDate now = LocalDate.now();
				Period diff = Period.between(date, now);
				
				em.addInlineField("Age", diff.getDays() + " Days, " + diff.getMonths() + " Months, " + diff.getYears() + " Years");
				event.getChannel().sendMessage(em);
			}
			
			return;
			
		} catch(SQLException ex) {
			event.getChannel().sendMessage(MessageUtil.message(Color.RED, "Something bad happened!", "There was a problem while communicating with the database server. Try again later."));
			return;
		}
	}

}
