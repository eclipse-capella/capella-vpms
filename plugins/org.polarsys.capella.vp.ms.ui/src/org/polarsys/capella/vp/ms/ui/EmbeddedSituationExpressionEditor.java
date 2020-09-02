/*******************************************************************************
 * Copyright (c) 2006, 2017 THALES GLOBAL SERVICES.
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

package org.polarsys.capella.vp.ms.ui;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.polarsys.capella.common.linkedtext.ui.DefaultLinkedTextContentProvider;
import org.polarsys.capella.common.linkedtext.ui.EmbeddedLinkedTextEditorConfiguration;
import org.polarsys.capella.common.linkedtext.ui.LinkedTextCompletionProcessor;
import org.polarsys.capella.common.linkedtext.ui.LinkedTextDocument;
import org.polarsys.capella.common.linkedtext.ui.LinkedTextHyperlink;
import org.polarsys.capella.core.data.capellacommon.AbstractState;

import com.google.common.base.Predicate;

/**
 * Adapted from {@link org.polarsys.capella.common.linkedtext.ui.EmbeddedLinkedTextEditor}
 * to allow fine tuning for expression edition.
 * TODO Refactor EmbeddedLinkedTextEditor for reusability here.
 */
public class EmbeddedSituationExpressionEditor {

  private SourceViewer sv;
  private LinkedTextDocument.Input _input;
  private LinkedTextDocument _document;
  private final String INITIAL_TEXT = "";

  public EmbeddedSituationExpressionEditor(Composite parent, int style) {
    sv = createSourceViewer(parent, style);
    sv.getTextWidget().addKeyListener(new InputKeyListener(sv));
    sv.getTextWidget().setAlwaysShowScrollBars(false);
    sv.getTextWidget().setToolTipText(INITIAL_TEXT);
  }

  protected Color getHintColor() {
    return sv.getTextWidget().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
  }

  /**
   * Set the input for this editor. This may currently be called just once, reuse is not supported 
   * and subsequent calls will throw IllegalStateException.
   * 
   * @param input the linked text editor input.
   */
  public void setInput(LinkedTextDocument.Input input) {
    if (_input != null){
      throw new IllegalStateException("Reuse of EmbeddedLinkedTextEditor instances is not yet supported"); //$NON-NLS-1$
    }
    _input = input;
    _document = LinkedTextDocument.load(_input);

    // save the document back to the model when changed
    _document.addDocumentListener(new IDocumentListener() {
      @Override
      public void documentChanged(final DocumentEvent event) {
        sv.getControl().getDisplay().asyncExec(new Runnable(){
          @Override
          public void run() {
            String content = _document.saveToRaw();
            _input.setText(content);
          }
        });
      }
      @Override
      public void documentAboutToBeChanged(DocumentEvent event) {
        //nop
      }
    });

    sv.getTextWidget().addDisposeListener(new DisposeListener() {
      @Override
      public void widgetDisposed(DisposeEvent e) {
        if (_document != null){
          _document.dispose();
        }
      }
    });

    sv.setDocument(_document, new AnnotationModel());
    sv.invalidateTextPresentation();
  }
  
  /**
   * Create and return the configuration used to configure the text viewer. This
   * implementation returns an {@link EmbeddedLinkedTextEditorConfiguration}. Subclasses
   * may override to return specialized behavior.
   */
  protected SourceViewerConfiguration createSourceViewerConfiguration(){
    return new EmbeddedLinkedTextEditorConfiguration() {

      @Override
      public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
        ContentAssistant ca = new ContentAssistant();
        final LinkedTextCompletionProcessor completionProcessor = new LinkedTextCompletionProcessor(
            new DefaultLinkedTextContentProvider(new Predicate<EObject>() {
              @Override
              public boolean apply(EObject arg) {
                if (arg instanceof AbstractState) {
                  return EcoreUtil.isAncestor((EObject) _input.getDocumentBase(), arg);
                }
                return false;
              }
            })) {

          private ICompletionProposal createProposal(ICompletionProposal delegate) {
            return new ICompletionProposal() {

              public void apply(IDocument document) {
                delegate.apply(document);
              }
              @Override
              public Point getSelection(IDocument document) {
                return delegate.getSelection(document);
              }
              @Override
              public String getAdditionalProposalInfo() {
                return delegate.getAdditionalProposalInfo();
              }

              @Override
              public String getDisplayString() {
                String[] elems = delegate.getDisplayString().split("/");
                return elems[elems.length - 3] + "/" + elems[elems.length - 1];
              }

              @Override
              public Image getImage() {
                return delegate.getImage();
              }

              @Override
              public IContextInformation getContextInformation() {
                return delegate.getContextInformation();
              }

            };
          }

          @Override
          public ICompletionProposal[] computeCompletionProposals(final ITextViewer viewer, final int offset) {
            ICompletionProposal[] result = null;
            ICompletionProposal[] inner = super.computeCompletionProposals(sourceViewer, offset);
            if (inner != null) {
              result = new ICompletionProposal[inner.length];
              for (int i = 0; i < inner.length; i++) {
                result[i] = createProposal(inner[i]);
              }
            }
            return result;
          }
        };
        ca.setContentAssistProcessor(completionProcessor, IDocument.DEFAULT_CONTENT_TYPE);
        ca.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
        ca.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
        ca.addCompletionListener(completionProcessor);
        return ca;
      }

    };
  }

  protected SourceViewer createSourceViewer(Composite parent, int styles) {

      final SourceViewer viewer= new SourceViewer(parent, null, null, true, styles);
      viewer.configure(createSourceViewerConfiguration());
      
      // when directly behind/before a hyperlink,
      // backspace and del should delete the entire link
      viewer.appendVerifyKeyListener(new VerifyKeyListener() {
        @Override
        public void verifyKey(VerifyEvent event) {
          if (event.keyCode == SWT.BS){
            final LinkedTextHyperlink hl = getHyperlinkOverOrBeforeCaret(viewer);
            if (hl != null){
              event.doit = false;
              postDelete(hl, event.display);
            }
          } else if (event.keyCode == SWT.DEL){
            final LinkedTextHyperlink hl = getHyperlinkOverOrAfterCaret(viewer);
            if (hl != null){
              event.doit = false;
              postDelete(hl, event.display);
            }
          }
        }
        
        private void postDelete(final LinkedTextHyperlink hl, Display display){
          display.asyncExec(new Runnable(){
            @Override
            public void run(){
              try {
                viewer.getDocument().replace(hl.getOffset(), hl.getLength(), ""); //$NON-NLS-1$
              } catch (BadLocationException exception) {
                exception.printStackTrace();
              }
              // FIXME, default position updater will shrink pos to size 0, not delete it.
              // so we must explicitly delete it
              hl.delete();
            }
          });
        }
      });

      // this blocks edition that would destroy hyperlinks
      viewer.getTextWidget().addVerifyListener(new VerifyListener() {
        @Override
        public void verifyText(VerifyEvent event) {
          LinkedTextDocument doc = (LinkedTextDocument) viewer.getInput();
          for (LinkedTextHyperlink h : doc.getHyperlinks()){
            if (h.overlapsWith(event.start, event.end - event.start)){
              // guard additional case of typing directly before a link 
              if (!(event.start == event.end && h.getOffset() == event.start)){
                event.doit = false;
              }
            }
          } 
        }
      });
      return viewer;
    }

    /*
     * Returns the position that the caret is over or directly after, possibly null.
     */
    private LinkedTextHyperlink getHyperlinkOverOrBeforeCaret(SourceViewer viewer){
      LinkedTextDocument doc = (LinkedTextDocument) viewer.getInput();
      int caretOffset = viewer.getTextWidget().getCaretOffset();
      for (LinkedTextHyperlink h : doc.getHyperlinks()){
        if (caretOffset > h.getOffset() && caretOffset <= h.getOffset() + h.getLength()){
          return h;
        }
      }
      return null;
    }

    /*
     * Returns the position that the caret is over or directly before, possibly null.
     */
    private LinkedTextHyperlink getHyperlinkOverOrAfterCaret(SourceViewer viewer){
      LinkedTextDocument doc = (LinkedTextDocument) viewer.getInput();
      int caretOffset = viewer.getTextWidget().getCaretOffset();
      for (LinkedTextHyperlink h : doc.getHyperlinks()){
        if (caretOffset >= h.getOffset() && caretOffset < h.getOffset() + h.getLength()){
          return h;
        }
      }
      return null;
    }

    private LinkedTextHyperlink getHyperlinkOverCaret(SourceViewer viewer){
      LinkedTextDocument doc = (LinkedTextDocument) viewer.getInput();
      int caretOffset = viewer.getTextWidget().getCaretOffset();
      for (LinkedTextHyperlink h : doc.getHyperlinks()){
        if (caretOffset > h.getOffset() && caretOffset < h.getOffset() + h.getLength()){
          return h;
        }
      }
      return null;
    }

    public SourceViewer getSourceViewer(){
      return sv;
    }

    /**
     * This opens the content assistant on Ctrl-Space, unless the caret is over a hyperlink.
     */
    private class InputKeyListener implements KeyListener {

      final SourceViewer _sv;

      InputKeyListener(SourceViewer viewer){
        _sv = viewer;
      }

      @Override
      public void keyReleased(KeyEvent e) {
        /*nop*/
      }

      @Override
      public void keyPressed(KeyEvent e) {
        switch (e.keyCode) {
          case ' ':
            if ((e.stateMask & SWT.CTRL) == SWT.CTRL && getHyperlinkOverCaret(_sv) == null) {
              _sv.doOperation(ISourceViewer.CONTENTASSIST_PROPOSALS);
            }
          }
      }
    }

}
