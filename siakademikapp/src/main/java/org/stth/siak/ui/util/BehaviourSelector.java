package org.stth.siak.ui.util;

import java.util.function.Consumer;

import com.github.appreciated.app.layout.behaviour.Behaviour;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class BehaviourSelector extends Window {
	
	private static final long serialVersionUID = 6532042814929940240L;

	public BehaviourSelector(Behaviour current, Consumer<Behaviour> consumer) {
        setModal(true);
        setClosable(true);
        setCaption("Select Behaviour");
        VerticalLayout layout = new VerticalLayout();
        setContent(layout);
        RadioButtonGroup<Behaviour> group = new RadioButtonGroup<>();
        group.addStyleName(ValoTheme.OPTIONGROUP_LARGE);
        group.setItems(
        		//Behaviour.LEFT,
               // Behaviour.LEFT_OVERLAY,
                Behaviour.LEFT_RESPONSIVE,
                Behaviour.LEFT_HYBRID,
               // Behaviour.LEFT_HYBRID_SMALL,
              //  Behaviour.LEFT_RESPONSIVE_HYBRID,
              //  Behaviour.LEFT_RESPONSIVE_HYBRID_NO_APP_BAR,
              //  Behaviour.LEFT_RESPONSIVE_HYBRID_OVERLAY_NO_APP_BAR,
               // Behaviour.LEFT_RESPONSIVE_OVERLAY,
              //  Behaviour.LEFT_RESPONSIVE_OVERLAY_NO_APP_BAR,
               // Behaviour.LEFT_RESPONSIVE_SMALL,
              //  Behaviour.LEFT_RESPONSIVE_SMALL_NO_APP_BAR,
                Behaviour.TOP
               // Behaviour.TOP_LARGE
                );
        group.setSelectedItem(current);
        layout.addComponent(group);
        group.addSelectionListener(singleSelectionEvent -> {
            consumer.accept(singleSelectionEvent.getSelectedItem().orElse(Behaviour.LEFT_RESPONSIVE));
            close();
        });
    }
}
