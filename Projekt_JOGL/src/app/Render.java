/**
 * 
 */
package app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.GLProfile;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * @author Erik
 * 
 */
public class Render implements GLEventListener {

	public static DisplayMode dm, dm_old;
	
	private GLU glu = new GLU();
	
	private float xrot;
	private float yrot;
	private float zrot;
	
	private int[] texture = new int[3];
	

	private float[] lightAmbient = { 0.5f, 0.5f, 0.5f, 1.0f };
	private float[] lightDiffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
	private float[] lightPosition = { 0.0f, 0.0f, 2.0f, 1.0f };
	private int filter;
	

	@SuppressWarnings("unused")
	private float xspeed, yspeed;
	private boolean light;

	private static JTextArea zadani = new JTextArea("A6 Ohyb tìlesa" + "\n"
			+ "\n" + "\b"
			+ "• vytvoøte program pro simulaci ohybu 3D tìlesa kruhového"
			+ " nebo ètvercového prùøezu " + "\n" + "\b"
			+ "• interaktivní zadání parametrù " + "\n" + "\b"
			+ "• výsledek zobrazit v dané transformaci a projekci" + "\b"
			+ "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "Autor:"
			+ "\n" + "\b" + "Erik Vojtìch" + "\n" + "\n" + "\n"
			+ "Vypracováno:" + "\n" + "\b" + "6. Dubna 2014");

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glLoadIdentity(); // view reset

		gl.glTranslatef(1.0f, -1.0f, -12.0f);

		gl.glRotatef(xrot + 15.0f, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yrot + 35.0f, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(zrot + 90.0f, 0.0f, 0.0f, 1.0f);
		
		gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[filter]);
		if (light)
			gl.glEnable(GL2.GL_LIGHTING);
		else
			gl.glDisable(GL2.GL_LIGHTING);

		gl.glBegin(GL2.GL_QUADS);
		
		// face 1
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(0.0f, -1.0f, 0.0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(2.0f, -1.0f, 0.0f);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(2.0f, -1.0f, 2.0f);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(0.0f, -1.0f, 2.0f);

		// face 2
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(2.0f, -1.0f, 2.0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(2.0f, -1.0f, 0.0f);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(2.0f, 5.0f, 0.0f);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(2.0f, 5.0f, 2.0f);
		
		// face 3
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(0.0f, 5.0f, 0.0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(0.0f, 5.0f, 2.0f);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(2.0f, 5.0f, 2.0f);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(2.0f, 5.0f, 0.0f);

		// face 4
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(0.0f, -1.0f, 2.0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(2.0f, -1.0f, 2.0f);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(2.0f, 5.0f, 2.0f);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(0.0f, 5.0f, 2.0f);
		
		// face 5
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(0.0f, -1.0f, 2.0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(0.0f, 5.0f, 2.0f);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(0.0f, 5.0f, 0.0f);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(0.0f, -1.0f, 0.0f);

		// face 6
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(0.0f, -1.0f, 0.0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(0.0f, 5.0f, 0.0f);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(2.0f, 5.0f, 0.0f);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(2.0f, -1.0f, 0.0f);
		gl.glEnd();

		gl.glFlush();
		
		 this.xrot -= .02; // dopredu/dozadu
		 this.yrot -= .02; // dokola
		 this.zrot -= .008; // doleva / doprava
	}

	
	
	@Override
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		
		
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL); // LINE / FILL
		gl.glClearColor(0f, 0.f, 0f, 0.5f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

		
		gl.glEnable(GL2.GL_TEXTURE_2D);
		try {
			File imgFile = new File("data/wood.png");
			Texture t = TextureIO.newTexture(imgFile, true);
			texture[0] = t.getTextureObject(gl);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
					GL2.GL_NEAREST);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
					GL2.GL_NEAREST);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[0]);

			t = TextureIO.newTexture(imgFile, true);
			texture[1] = t.getTextureObject(gl);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
					GL2.GL_LINEAR);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
					GL2.GL_LINEAR);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[1]);

			t = TextureIO.newTexture(imgFile, true);
			texture[2] = t.getTextureObject(gl);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
					GL2.GL_LINEAR);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
					GL2.GL_LINEAR_MIPMAP_NEAREST);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[2]);
			/**
			 * Lighting
			 */

			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, this.lightAmbient, 0);
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, this.lightDiffuse, 0);
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, this.lightPosition, 0);

			gl.glEnable(GL2.GL_LIGHT1);
			gl.glEnable(GL2.GL_LIGHTING);

			this.light = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		final GL2 gl = drawable.getGL().getGL2();
		if (height <= 0)
			height = 1;
		final float h = (float) width / (float) height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0f, h, 1.0, 20.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// opening GL
		
		final GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);

		// The canvas
		final GLCanvas glcanvas = new GLCanvas(capabilities);
		Render r = new Render();
		glcanvas.addGLEventListener(r);
		glcanvas.setSize(600, 600);

		final FPSAnimator animator = new FPSAnimator(glcanvas, 300,true );
		
		final JFrame frame = new JFrame ("JOGL projekt");
		
		// ZADANI
		JPanel textP = new JPanel();

		zadani.setFont(new Font("Serif", Font.PLAIN, 16));
		zadani.setLineWrap(true);
		zadani.setWrapStyleWord(true);
		zadani.setPreferredSize(new Dimension(460, 460));
		zadani.setMargin(new Insets(25, 25, 25, 25));
		zadani.setEditable(false);

		textP.add(zadani);

		
/*
		//DEFORM MENU
		JPanel defP = new JPanel();
		defP.setPreferredSize(new Dimension(35, 35));
		JButton btnDeform = new JButton("Ohyb");
		defP.add(btnDeform);
		
		frame.add(defP, BorderLayout.EAST);
		*/
		// TAB MENU
		JTabbedPane tabMenu = new JTabbedPane();
		tabMenu.addTab("Render", glcanvas);
		tabMenu.addTab("Zadání", textP);
		frame.add(tabMenu, BorderLayout.NORTH);

		// RENDER
				JPanel p = new JPanel();
				p.setPreferredSize(new Dimension(0, 0));
				frame.add(p, BorderLayout.WEST);
		
				keyBindings(p, frame, r);
				
		// shutdown
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (animator.isStarted())
					animator.stop();
				System.exit(0);
			}
		});
		frame.setSize(frame.getContentPane().getPreferredSize());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		animator.start();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {

	}

	
	private static void keyBindings(JPanel p, final JFrame frame, final Render r) {

		ActionMap actionMap = p.getActionMap();
		InputMap inputMap = p.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		/*
		 * 
		 */
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), "L");
		actionMap.put("L", new AbstractAction() {

			private static final long serialVersionUID = -6576101918414437189L;

			@Override
			public void actionPerformed(ActionEvent drawable) { 
				if (r.light)
					r.light = false;
				else
					r.light = true;
			}
		});
	}
}
