package org.polarsys.capella.vp.ms.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.polarsys.capella.common.linkedtext.ui.LinkedTextDocument;

public class SituationEditorDialog extends Dialog {

    private final LinkedTextDocument.Input input;
    private EmbeddedSituationExpressionEditor editor;
    private IDocument document;
    
    public SituationEditorDialog(Shell parentShell, LinkedTextDocument.Input input) {
        super(parentShell);
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
        editor = new EmbeddedSituationExpressionEditor(container, SWT.NONE);
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