/*
 *  JOrtho
 *
 *  Copyright (C) 2005-2008 by i-net software
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as 
 *  published by the Free Software Foundation; either version 2 of the
 *  License, or (at your option) any later version. 
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 *  USA.
 *  
 *  Created on 05.12.2007
 */

/* 
Portions (identified below) were modified by or written by the Miradi team, 
working for Foundations of Success, Bethesda, Maryland (on behalf of 
the Conservation Measures Partnership, "CMP") and Beneficent Technology, 
Inc. ("Benetech"), Palo Alto, California.

These portions may be released under the same terms as the original 
file, specifically either under the GNU GPL, or by i-net software 
under their commercial license.
*/ 

package com.inet.jortho;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;

public class MiradiCheckerListener implements PopupMenuListener, LanguageChangeListener {

    private final JComponent          menu;

    private Dictionary                dictionary;

    private Locale                    locale;

    private final SpellCheckerOptions options;

    MiradiCheckerListener( JComponent menu, SpellCheckerOptions options ) {
        this.menu = menu;
        this.options = options == null ? SpellChecker.getOptions() : options;
        SpellChecker.addLanguageChangeLister( this );
        dictionary = SpellChecker.getCurrentDictionary();
        locale = SpellChecker.getCurrentLocale();
    }

    public void popupMenuCanceled( PopupMenuEvent e ) {
        /* empty */
    }

    public void popupMenuWillBecomeInvisible( PopupMenuEvent e ) {
        /* empty */
    }

    public void popupMenuWillBecomeVisible( PopupMenuEvent ev ) {
        JPopupMenu popup = (JPopupMenu)ev.getSource();

        Component invoker = popup.getInvoker();
        if( invoker instanceof JTextComponent ) {
            populateSpellCheckerMenu(invoker);
        }
    }

	private void populateSpellCheckerMenu(Component invoker)
	{
		final JTextComponent jText = (JTextComponent)invoker;
		if( !jText.isEditable() ) {
		    // Suggestions only for editable text components
		    menu.setEnabled( false );
		    return;
		}

	    if( dictionary == null ) {
	        // without dictionary it is disabled
	        menu.setEnabled( false );
	        return;
	    }

		try {
			int offs = getOffsetOfStartOfWordAtCursor(jText);
		    
		    if( offs < 0 ) {
		        // occur if there nothing under the mouse pointer
		        menu.setEnabled( false );
		        return;
		    }
		    
		    // get the word from current position
		    final int begOffs = Utilities.getWordStart( jText, offs );
		    final int endOffs = Utilities.getWordEnd( jText, offs );
		    final String word = jText.getText( begOffs, endOffs - begOffs );

		    //find the first invalid word from current position
		    Tokenizer tokenizer = new Tokenizer( jText, dictionary, locale, offs, options );
		    String invalidWord;
		    do {
		        invalidWord = tokenizer.nextInvalidWord();
		    } while( tokenizer.getWordOffset() < begOffs );

		    if( !word.equals( invalidWord ) ) {
		        // the current word is not invalid
		        menu.setEnabled( false );
		        return;
		    }

		    menu.removeAll();

			List<Suggestion> list = dictionary.searchSuggestions( word );

			boolean needCapitalization = tokenizer.isFirstWordInSentence() && Utils.isFirstCapitalized( word );
		    Vector<JMenuItem> menuItems = getSpellCheckerMenuItems(jText, begOffs, list, needCapitalization);

			//Disable the menu if it is empty
			if(menuItems.size() == 0)
			{
				menu.setEnabled( false );
				return;
			}

			for(JMenuItem menuItem : menuItems)
			{
				menu.add(menuItem);
			}
			menu.setEnabled( true );
		} catch( BadLocationException ex ) {
		    ex.printStackTrace();
		}
		return;
	}

	private Vector<JMenuItem> getSpellCheckerMenuItems(final JTextComponent jText,
			final int begOffs, List<Suggestion> list, boolean needCapitalization) throws BadLocationException
	{
		Vector<JMenuItem> menuItems = new Vector<JMenuItem>();
		
	    final int endOffs = Utilities.getWordEnd( jText, begOffs );

		for( int i = 0; i < list.size() && i < options.getSuggestionsLimitMenu(); i++ ) {
		    Suggestion sugestion = list.get( i );
		    String sugestionWord = sugestion.getWord();
		    if( needCapitalization ) {
		        sugestionWord = Utils.getCapitalized( sugestionWord );
		    }
		    JMenuItem item = new JMenuItem( sugestionWord );
		    menuItems.add( item );
		    final String newWord = sugestionWord;
		    item.addActionListener( new ActionListener() {

		        public void actionPerformed( ActionEvent e ) {
		            jText.setSelectionStart( begOffs );
		            jText.setSelectionEnd( endOffs );
		            jText.replaceSelection( newWord );
		        }

		    } );
		}

		return menuItems;
	}

	private int getOffsetOfStartOfWordAtCursor(final JTextComponent jText)
			throws BadLocationException
	{
		Caret caret = jText.getCaret();
		int offs = Math.min( caret.getDot(), caret.getMark() );
		Point p = jText.getMousePosition();
		if( p != null ) {
		    // use position from mouse click and not from editor cursor position 
		    offs = jText.viewToModel( p );
		}
		Document doc = jText.getDocument();
		if( offs > 0 && (offs >= doc.getLength() || Character.isWhitespace( doc.getText( offs, 1 ).charAt( 0 ) )) ) {
		    // if the next character is a white space then use the word on the left site
		    offs--;
		}
		return offs;
	}

    public void languageChanged( LanguageChangeEvent ev ) {
        dictionary = SpellChecker.getCurrentDictionary();
        locale = SpellChecker.getCurrentLocale();
    }
}