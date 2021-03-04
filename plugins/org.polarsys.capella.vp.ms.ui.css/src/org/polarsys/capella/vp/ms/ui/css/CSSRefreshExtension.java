/*******************************************************************************
 * Copyright (c) 2020 THALES GLOBAL SERVICES.
 *  
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.vp.ms.ui.css;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.ui.css.core.engine.CSSErrorHandler;
import org.eclipse.emf.common.util.URI;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtension;
import org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtensionProvider;
import org.eclipse.sirius.diagram.description.Layer;
import org.eclipse.sirius.viewpoint.RGBValues;
import org.polarsys.capella.vp.ms.ui.css.engine.DiagramCSSEngine;
import org.w3c.dom.stylesheets.StyleSheet;


public class CSSRefreshExtension implements IRefreshExtensionProvider, IRefreshExtension {

  private static RGBValues conflictColor= RGBValues.create(255, 165, 0);
  private static RGBValues excludeColor= RGBValues.create(220, 220, 220);
  private static RGBValues excludeForegroundColor = RGBValues.create(200, 200, 200);
  private static RGBValues excludeBackroundColor = RGBValues.create(200, 200, 200);
  private static RGBValues excludeLabelColor = RGBValues.create(120, 120, 120);
  

  @Override
  public boolean provides(DDiagram diagram) {
    return true;
  }

  @Override
  public IRefreshExtension getRefreshExtension(DDiagram viewPoint) {
    return this;
  }

  @Override
  public void beforeRefresh(DDiagram dDiagram) {
  }

  @Override
  public void postRefresh(DDiagram dDiagram) {

    Session session = SessionManager.INSTANCE.getSession(dDiagram);
    URI workspaceBaseURI = session.getSessionResource().getURI().trimSegments(1);
    
    DiagramCSSEngine engine = new DiagramCSSEngine();
    engine.setErrorHandler(new CSSErrorHandler() {
      @Override
      public void error(Exception e) {
        e.printStackTrace();
      }
    });
    
    Collection<URI> seen = new HashSet<>();

    for (Layer layer : dDiagram.getActivatedLayers()) {
      URI resource = layer.eResource().getURI();
      if (seen.add(resource)) {
        URI cssURI = resource.trimFileExtension().appendFileExtension("css");
        URI customCSSURI = workspaceBaseURI.appendSegment(cssURI.segment(cssURI.segmentCount() - 1));
        String customCSSPlatformString = customCSSURI.toPlatformString(true);
        IFile customCSSFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(customCSSPlatformString));
        try {
          URL cssURL = FileLocator.find(new URL(cssURI.toString()));
          if (cssURL != null) {
            try (InputStream is = cssURL.openStream()){
              StyleSheet ss = engine.parseStyleSheet(is);
            }
          }
                    
          if (customCSSFile.exists()) {     
            try (InputStream is = customCSSFile.getContents()){
              engine.parseStyleSheet(is);
            } catch (CoreException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }
          }

        } catch (MalformedURLException e) {
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

   
      engine.applyStyles(dDiagram, true);
  }

//  public void applyStyles(DDiagramElement element) {
//   
//    CSConfigurationStyle style = (CSConfigurationStyle) EcoreUtil.getExistingAdapter(element, CSConfigurationStyle.class);
//    
//    if (style != null) {
//      
//      //System.out.println(EObjectLabelProviderHelper.getText(element.getTarget()) + " " + style.getStyle());
//
//      if (CsConfigurationServices.isConsistentIncludeRequired()) {
//
//        // all configs must include the element, otherwise it's greyed out
//        
//        if (style.hasClass("excluded")) {
//        
//          if (CsConfigurationServices.isMarkConflictingInclusions() && style.hasClass("included")) {
//            
//            applyConflict(element);
//            
//          } else {
//
//            applyExclude(element);
//           
//          }
//          
//        }
//        
//      } else {
//       
//        // at least one config must include the element, otherwise it's greyed out
//        if (!style.hasClass("included") && style.hasClass("excluded")) {
//          
//          applyExclude(element);
//          
//        }
//
//      }
//      
//    }
//    
//  
//  }
//  
//  private void applyConflict(DDiagramElement element) {
//
//    Style style = element.getStyle();
//   
//    if (style instanceof Square) {
//      ((Square) style).setColor(conflictColor);
//    } else if (style instanceof EdgeStyle) {
//      ((EdgeStyle) style).setStrokeColor(conflictColor);
//    } else {
//      applyExclude(element);
//    }
//  }
//  
//  private void applyExclude(DDiagramElement element) {
//    
//    Style style = element.getStyle();
//    if (style instanceof BasicLabelStyle) {
//      ((BasicLabelStyle)style).setLabelColor(excludeLabelColor);
//    }
//
//    if (style instanceof FlatContainerStyle) {
//      ((FlatContainerStyle)style).setBackgroundColor(excludeBackroundColor);
//      ((FlatContainerStyle)style).setForegroundColor(excludeForegroundColor);
//    } else if (style instanceof Square) {
//      ((Square) style).setColor(excludeColor);
//    } else if (style instanceof EdgeStyle) {
//      ((EdgeStyle) style).setStrokeColor(excludeColor);
//      if (((EdgeStyle)style).getCenterLabelStyle() != null){
//        ((EdgeStyle) style).getCenterLabelStyle().setLabelColor(excludeLabelColor);
//      }
//    } else if (style instanceof WorkspaceImage) {
//      ((WorkspaceImage) style).setWorkspacePath(Images.getImagePath(element.getTarget(), element));
//    }
//  }
//  
//  /**
//   * Finds the style object associated to the given view
//   * @param view the view
//   * @return the style object for the view argument. never null.
//   */
//  public CSConfigurationStyle getCSConfigurationStyle(EObject view) {
//    CSConfigurationStyle style = (CSConfigurationStyle) EcoreUtil.getExistingAdapter(view, CSConfigurationStyle.class);
//    if (style == null) {
//      style = new CSConfigurationStyle();
//      view.eAdapters().add(style);
//    }
//    return style;
//  }

}
