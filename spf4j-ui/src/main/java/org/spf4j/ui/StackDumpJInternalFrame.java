/*
 * Copyright (c) 2001, Zoltan Farkas All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
//CHECKSTYLE:OFF
package org.spf4j.ui;

import com.google.protobuf.CodedInputStream;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import org.spf4j.stackmonitor.SampleNode;
import org.spf4j.stackmonitor.proto.Converter;
import org.spf4j.stackmonitor.proto.gen.ProtoSampleNodes;

/**
 * will need to add some standard filtering:
 * 
 * Pair.of(sun.misc.Unsafe.class.getName(), "park"));
 * Pair.of(java.lang.Object.class.getName(), "wait"));
 * Pair.of(java.lang.Thread.class.getName(), "sleep"));
 * Pair.of("java.net.PlainSocketImpl", "socketAccept"));
 * Pair.of("java.net.PlainSocketImpl", "socketConnect"));
 * @author zoly
 */
@SuppressFBWarnings({"FCBL_FIELD_COULD_BE_LOCAL", "SE_BAD_FIELD" })
public class StackDumpJInternalFrame extends javax.swing.JInternalFrame {

    private SampleNode samples;

    /**
     * Creates new form StackDumpJInternalFrame
     */
    public StackDumpJInternalFrame(final String sampleFile, final boolean isPro) throws IOException {
        super(sampleFile);
        initComponents();
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sampleFile));
        final CodedInputStream is = CodedInputStream.newInstance(bis);
        is.setRecursionLimit(Short.MAX_VALUE);
        try {
            samples = Converter.fromProtoToSampleNode(ProtoSampleNodes.SampleNode.parseFrom(is));
        } finally {
            bis.close();
        }
        if (isPro) {
            ssScrollPanel.setViewportView(new ZStackPanel(samples));
        } else {
            ssScrollPanel.setViewportView(new FlameStackPanel(samples));            
        }
        ssScrollPanel.setVisible(true);
        pack();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    @SuppressFBWarnings
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ssScrollPanel = new javax.swing.JScrollPane();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        ssScrollPanel.setAutoscrolls(true);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(ssScrollPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(ssScrollPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane ssScrollPanel;
    // End of variables declaration//GEN-END:variables
}
