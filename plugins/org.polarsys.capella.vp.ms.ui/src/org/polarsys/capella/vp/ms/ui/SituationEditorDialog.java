package org.polarsys.capella.vp.ms.ui;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.polarsys.capella.common.linkedtext.ui.DefaultLinkedTextContentProvider;
import org.polarsys.capella.common.linkedtext.ui.EmbeddedLinkedTextEditor;
import org.polarsys.capella.common.linkedtext.ui.EmbeddedLinkedTextEditorConfiguration;
import org.polarsys.capella.common.linkedtext.ui.LinkedTextCompletionProcessor;
import org.polarsys.capella.common.linkedtext.ui.LinkedTextCompletionProposal;
import org.polarsys.capella.common.linkedtext.ui.LinkedTextDocument;
import org.polarsys.capella.core.data.capellacommon.AbstractState;
import org.polarsys.capella.core.data.capellacommon.StateMachine;
import org.polarsys.capella.core.data.cs.Component;
import org.polarsys.capella.core.linkedtext.ui.CapellaEmbeddedLinkedTextEditorInput;
import org.polarsys.capella.vp.ms.Situation;

import com.google.common.base.Predicate;

public class SituationEditorDialog extends Dialog {

    private final StateMachine sm;
    private final LinkedTextDocument.Input input;
    private EmbeddedLinkedTextEditor editor;
    private IDocument document;
    
    public SituationEditorDialog(Shell parentShell, StateMachine stateMachine, LinkedTextDocument.Input input) {
        super(parentShell);
        this.sm = stateMachine;
        this.input = input;
    }
    
    public LinkedTextDocument getDocument() {
      return (LinkedTextDocument) document;
    }
    
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);

        // a fine-tuned linked-text editor:
        //  * only proposes states of the owned state machines of the edited line
        //  * proposals should be name_of_statemachine/state
        editor = new EmbeddedLinkedTextEditor(container, SWT.NONE) {
          @Override
          protected SourceViewerConfiguration createSourceViewerConfiguration() {
            return new EmbeddedLinkedTextEditorConfiguration() {

              @Override
              public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
                ContentAssistant ca = new ContentAssistant();
                final LinkedTextCompletionProcessor completionProcessor = new LinkedTextCompletionProcessor(
                    new DefaultLinkedTextContentProvider(new Predicate<EObject>() {
                      @Override
                      public boolean apply(EObject arg) {
                        if (arg instanceof AbstractState) {
                          return EcoreUtil.isAncestor(sm, arg);
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
        };
        editor.getSourceViewer().getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        editor.setInput(input);
        document = editor.getSourceViewer().getDocument();
        
        return container;
    }

    // overriding this methods allows you to set the
    // title of the custom dialog
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Selection dialog");
    }

    @Override
    protected void okPressed() {
      super.okPressed();
    }

    @Override
    protected Point getInitialSize() {
        return new Point(450, 300);
    }

}