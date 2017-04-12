/**
 * This file Copyright (c) 2017 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This program and the accompanying materials are made
 * available under the terms of the Magnolia Network Agreement
 * which accompanies this distribution, and is available at
 * http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.field;

import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.predicate.NodeTypePredicate;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.ui.api.context.UiContext;
import info.magnolia.ui.api.i18n.I18NAuthoringSupport;
import info.magnolia.ui.form.field.definition.SelectFieldOptionDefinition;
import info.magnolia.ui.form.field.factory.SelectFieldFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Item;

/**
 * @author ebguilbert
 */
public class ContactSelectFieldFactory extends SelectFieldFactory<ContactSelectFieldDefinition> {

    private static final Logger log = LoggerFactory.getLogger(ContactSelectFieldFactory.class);

    /**
     * @param definition
     * @param relatedFieldItem
     * @param uiContext
     * @param i18nAuthoringSupport
     */
    public ContactSelectFieldFactory(ContactSelectFieldDefinition definition, Item relatedFieldItem, UiContext uiContext, I18NAuthoringSupport i18nAuthoringSupport) {
        super(definition, relatedFieldItem, uiContext, i18nAuthoringSupport);
    }

    @Override
    public List<SelectFieldOptionDefinition> getSelectFieldOptionDefinition() {
        List<SelectFieldOptionDefinition> selectOptions = new ArrayList<SelectFieldOptionDefinition>();

        try {
            Session jcrSession = MgnlContext.getJCRSession("contacts");
            if (jcrSession != null) {
                Iterable<Node> childrenIterable = NodeUtil.collectAllChildren(jcrSession.getRootNode(), new NodeTypePredicate("mgnl:contact", true));

                for (Iterator<Node> iterator = childrenIterable.iterator(); iterator.hasNext();) {
                    Node node = iterator.next();
                    SelectFieldOptionDefinition option = new SelectFieldOptionDefinition();
                    option.setValue(node.getIdentifier());
                    option.setLabel(PropertyUtil.getString(node, "lastName") + " " + PropertyUtil.getString(node, "firstName"));
                    selectOptions.add(option);
                }
            }
        } catch (LoginException e) {
            log.error("User has no rights on item: ", e.getMessage());
        } catch (RepositoryException e1) {
            log.error("Data could not be fetched form JCR: " + e1.getMessage());
        }

        return selectOptions;
    }

}
