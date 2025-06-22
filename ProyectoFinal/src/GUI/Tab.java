/**
 * PROYECTO FINAL
 * AUTORES:
 * Juan Sebastian Bravo Rojas  20241020004
 * Juan Estevan Ariza Ortiz  20241020005
 * Jhoe Luis Jeanpaul Miranda Alvarez 20241020022
 * Fecha: 9 de Julio del 2025
 */
package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class Tab extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton[][] tablero = new JButton[8][8];

	/**
	 * Create the frame.
	 */
	public Tab() {
		setTitle("Chess");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 970, 816);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(210, 180, 140));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.GROWING_BUTTON_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.GROWING_BUTTON_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.GROWING_BUTTON_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.GROWING_BUTTON_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.GROWING_BUTTON_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.GROWING_BUTTON_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.GROWING_BUTTON_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.GROWING_BUTTON_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewLabel = new JLabel("A");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setForeground(new Color(255, 250, 250));
		contentPane.add(lblNewLabel, "4, 2");
		lblNewLabel.setHorizontalAlignment((int) CENTER_ALIGNMENT);
		
		JLabel lblNewLabel_1 = new JLabel("B");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setForeground(new Color(255, 250, 250));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1, "6, 2");
		
		JLabel lblNewLabel_1_1 = new JLabel("C");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setForeground(new Color(255, 250, 250));
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1, "8, 2");
		
		JLabel lblNewLabel_1_2 = new JLabel("D");
		lblNewLabel_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2.setForeground(new Color(255, 250, 250));
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_2, "10, 2");
		
		JLabel lblNewLabel_1_3 = new JLabel("E");
		lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3.setForeground(new Color(255, 250, 250));
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_3, "12, 2");
		
		JLabel lblNewLabel_1_4 = new JLabel("F");
		lblNewLabel_1_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_4.setForeground(new Color(255, 250, 250));
		lblNewLabel_1_4.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_4, "14, 2");
		
		JLabel lblNewLabel_1_5 = new JLabel("G");
		lblNewLabel_1_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_5.setForeground(new Color(255, 250, 250));
		lblNewLabel_1_5.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_5, "16, 2");
		
		JLabel lblNewLabel_1_6 = new JLabel("H");
		lblNewLabel_1_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_6.setForeground(new Color(255, 250, 250));
		lblNewLabel_1_6.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_6, "18, 2");
		
		JLabel lblNewLabel_2 = new JLabel("8");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setForeground(new Color(255, 250, 250));
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_2, "2, 4");
		
		JButton BTN_A8 = new JButton("");
		BTN_A8.setBackground(new Color(255, 248, 220));
		BTN_A8.setContentAreaFilled(false);
		BTN_A8.setOpaque(true);
		BTN_A8.setBorderPainted(false);
		contentPane.add(BTN_A8, "4, 4, 2, 2");
		
		JButton BTN_B8 = new JButton("");
		BTN_B8.setBackground(new Color(184, 134, 11));
		BTN_B8.setContentAreaFilled(false);
		BTN_B8.setOpaque(true);
		BTN_B8.setBorderPainted(false);
		contentPane.add(BTN_B8, "6, 4, 2, 2");
		
		JButton BTN_C8 = new JButton("");
		BTN_C8.setBackground(new Color(255, 248, 220));
		BTN_C8.setContentAreaFilled(false);
		BTN_C8.setOpaque(true);
		BTN_C8.setBorderPainted(false);
		contentPane.add(BTN_C8, "8, 4, 2, 2");
		
		JButton BTN_D8 = new JButton("");
		BTN_D8.setBackground(new Color(184, 134, 11));
		BTN_D8.setContentAreaFilled(false);
		BTN_D8.setOpaque(true);
		BTN_D8.setBorderPainted(false);
		contentPane.add(BTN_D8, "10, 4, 2, 2");
		
		JButton BTN_E8 = new JButton("");
		BTN_E8.setBackground(new Color(255, 248, 220));
		BTN_E8.setContentAreaFilled(false);
		BTN_E8.setOpaque(true);
		BTN_E8.setBorderPainted(false);
		contentPane.add(BTN_E8, "12, 4, 2, 2");
		
		JButton BTN_F8 = new JButton("");
		BTN_F8.setBackground(new Color(184, 134, 11));
		BTN_F8.setContentAreaFilled(false);
		BTN_F8.setOpaque(true);
		BTN_F8.setBorderPainted(false);
		contentPane.add(BTN_F8, "14, 4, 2, 2");
		
		JButton BTN_G8 = new JButton("");
		BTN_G8.setBackground(new Color(255, 248, 220));
		BTN_G8.setContentAreaFilled(false);
		BTN_G8.setOpaque(true);
		BTN_G8.setBorderPainted(false);
		contentPane.add(BTN_G8, "16, 4, 2, 2");
		
		JButton BTN_H8 = new JButton("");
		BTN_H8.setBackground(new Color(184, 134, 11));
		BTN_H8.setContentAreaFilled(false);
		BTN_H8.setOpaque(true);
		BTN_H8.setBorderPainted(false);
		contentPane.add(BTN_H8, "18, 4, 2, 2");
		
		JLabel lblNewLabel_3 = new JLabel("7");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setForeground(new Color(255, 250, 250));
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_3, "2, 6");
		
		JButton BTN_A7 = new JButton("");
		BTN_A7.setBackground(new Color(184, 134, 11));
		BTN_A7.setContentAreaFilled(false);
		BTN_A7.setOpaque(true);
		BTN_A7.setBorderPainted(false);
		contentPane.add(BTN_A7, "4, 6, 2, 2");
		
		JButton BTN_B7 = new JButton("");
		BTN_B7.setBackground(new Color(255, 248, 220));
		BTN_B7.setContentAreaFilled(false);
		BTN_B7.setOpaque(true);
		BTN_B7.setBorderPainted(false);
		contentPane.add(BTN_B7, "6, 6, 2, 2");
		
		JButton BTN_C7 = new JButton("");
		BTN_C7.setBackground(new Color(184, 134, 11));
		BTN_C7.setContentAreaFilled(false);
		BTN_C7.setOpaque(true);
		BTN_C7.setBorderPainted(false);
		contentPane.add(BTN_C7, "8, 6, 2, 2");
		
		JButton BTN_D7 = new JButton("");
		BTN_D7.setBackground(new Color(255, 248, 220));
		BTN_D7.setContentAreaFilled(false);
		BTN_D7.setOpaque(true);
		BTN_D7.setBorderPainted(false);
		contentPane.add(BTN_D7, "10, 6, 2, 2");
		
		JButton BTN_E7 = new JButton("");
		BTN_E7.setBackground(new Color(184, 134, 11));
		BTN_E7.setContentAreaFilled(false);
		BTN_E7.setOpaque(true);
		BTN_E7.setBorderPainted(false);
		contentPane.add(BTN_E7, "12, 6, 2, 2");
		
		JButton BTN_F7 = new JButton("");
		BTN_F7.setBackground(new Color(255, 248, 220));
		BTN_F7.setContentAreaFilled(false);
		BTN_F7.setOpaque(true);
		BTN_F7.setBorderPainted(false);
		contentPane.add(BTN_F7, "14, 6, 2, 2");
		
		JButton BTN_G7 = new JButton("");
		BTN_G7.setBackground(new Color(184, 134, 11));
		BTN_G7.setContentAreaFilled(false);
		BTN_G7.setOpaque(true);
		BTN_G7.setBorderPainted(false);
		contentPane.add(BTN_G7, "16, 6, 2, 2");
		
		JButton BTN_H7 = new JButton("");
		BTN_H7.setBackground(new Color(255, 248, 220));
		BTN_H7.setContentAreaFilled(false);
		BTN_H7.setOpaque(true);
		BTN_H7.setBorderPainted(false);
		contentPane.add(BTN_H7, "18, 6, 2, 2");
		
		JLabel lblNewLabel_4 = new JLabel("6");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setForeground(new Color(255, 250, 250));
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_4, "2, 8");
		
		JButton BTN_A6 = new JButton("");
		BTN_A6.setBackground(new Color(255, 248, 220));
		BTN_A6.setContentAreaFilled(false);
		BTN_A6.setOpaque(true);
		BTN_A6.setBorderPainted(false);
		contentPane.add(BTN_A6, "4, 8, 2, 2");
		
		JButton BTN_B6 = new JButton("");
		BTN_B6.setBackground(new Color(184, 134, 11));
		BTN_B6.setContentAreaFilled(false);
		BTN_B6.setOpaque(true);
		BTN_B6.setBorderPainted(false);
		contentPane.add(BTN_B6, "6, 8, 2, 2");
		
		JButton BTN_C6 = new JButton("");
		BTN_C6.setBackground(new Color(255, 248, 220));
		BTN_C6.setContentAreaFilled(false);
		BTN_C6.setOpaque(true);
		BTN_C6.setBorderPainted(false);
		contentPane.add(BTN_C6, "8, 8, 2, 2");
		
		JButton BTN_D6 = new JButton("");
		BTN_D6.setBackground(new Color(184, 134, 11));
		BTN_D6.setContentAreaFilled(false);
		BTN_D6.setOpaque(true);
		BTN_D6.setBorderPainted(false);
		contentPane.add(BTN_D6, "10, 8, 2, 2");
		
		JButton BTN_E6 = new JButton("");
		BTN_E6.setBackground(new Color(255, 248, 220));
		BTN_E6.setContentAreaFilled(false);
		BTN_E6.setOpaque(true);
		BTN_E6.setBorderPainted(false);
		contentPane.add(BTN_E6, "12, 8, 2, 2");
		
		JButton BTN_F6 = new JButton("");
		BTN_F6.setBackground(new Color(184, 134, 11));
		BTN_F6.setContentAreaFilled(false);
		BTN_F6.setOpaque(true);
		BTN_F6.setBorderPainted(false);
		contentPane.add(BTN_F6, "14, 8, 2, 2");
		
		JButton BTN_G6 = new JButton("");
		BTN_G6.setBackground(new Color(255, 248, 220));
		BTN_G6.setContentAreaFilled(false);
		BTN_G6.setOpaque(true);
		BTN_G6.setBorderPainted(false);
		contentPane.add(BTN_G6, "16, 8, 2, 2");
		
		JButton BTN_H6 = new JButton("");
		BTN_H6.setBackground(new Color(184, 134, 11));
		BTN_H6.setContentAreaFilled(false);
		BTN_H6.setOpaque(true);
		BTN_H6.setBorderPainted(false);
		contentPane.add(BTN_H6, "18, 8, 2, 2");
		
		JLabel lblNewLabel_2_1 = new JLabel("5");
		lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1.setForeground(new Color(255, 250, 250));
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_2_1, "2, 10");
		
		JButton BTN_A5 = new JButton("");
		BTN_A5.setBackground(new Color(184, 134, 11));
		BTN_A5.setContentAreaFilled(false);
		BTN_A5.setOpaque(true);
		BTN_A5.setBorderPainted(false);
		contentPane.add(BTN_A5, "4, 10, 2, 2");
		
		JButton BTN_B5 = new JButton("");
		BTN_B5.setBackground(new Color(255, 248, 220));
		BTN_B5.setContentAreaFilled(false);
		BTN_B5.setOpaque(true);
		BTN_B5.setBorderPainted(false);
		contentPane.add(BTN_B5, "6, 10, 2, 2");
		
		JButton BTN_C5 = new JButton("");
		BTN_C5.setBackground(new Color(184, 134, 11));
		BTN_C5.setContentAreaFilled(false);
		BTN_C5.setOpaque(true);
		BTN_C5.setBorderPainted(false);
		contentPane.add(BTN_C5, "8, 10, 2, 2");
		
		JButton BTN_D5 = new JButton("");
		BTN_D5.setBackground(new Color(255, 248, 220));
		BTN_D5.setContentAreaFilled(false);
		BTN_D5.setOpaque(true);
		BTN_D5.setBorderPainted(false);
		contentPane.add(BTN_D5, "10, 10, 2, 2");
		
		JButton BTN_E5 = new JButton("");
		BTN_E5.setBackground(new Color(184, 134, 11));
		BTN_E5.setContentAreaFilled(false);
		BTN_E5.setOpaque(true);
		BTN_E5.setBorderPainted(false);
		contentPane.add(BTN_E5, "12, 10, 2, 2");
		
		JButton BTN_F5 = new JButton("");
		BTN_F5.setBackground(new Color(255, 248, 220));
		BTN_F5.setContentAreaFilled(false);
		BTN_F5.setOpaque(true);
		BTN_F5.setBorderPainted(false);
		contentPane.add(BTN_F5, "14, 10, 2, 2");
		
		JButton BTN_G5 = new JButton("");
		BTN_G5.setBackground(new Color(184, 134, 11));
		BTN_G5.setContentAreaFilled(false);
		BTN_G5.setOpaque(true);
		BTN_G5.setBorderPainted(false);
		contentPane.add(BTN_G5, "16, 10, 2, 2");
		
		JButton BTN_H5 = new JButton("");
		BTN_H5.setBackground(new Color(255, 248, 220));
		BTN_H5.setContentAreaFilled(false);
		BTN_H5.setOpaque(true);
		BTN_H5.setBorderPainted(false);
		contentPane.add(BTN_H5, "18, 10, 2, 2");
		
		JLabel lblNewLabel_2_2 = new JLabel("4");
		lblNewLabel_2_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_2.setForeground(new Color(255, 250, 250));
		lblNewLabel_2_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_2_2, "2, 12");
		
		JButton BTN_A4 = new JButton("");
		BTN_A4.setBackground(new Color(255, 248, 220));
		BTN_A4.setContentAreaFilled(false);
		BTN_A4.setOpaque(true);
		BTN_A4.setBorderPainted(false);
		contentPane.add(BTN_A4, "4, 12, 2, 2");
		
		JButton BTN_B4 = new JButton("");
		BTN_B4.setBackground(new Color(184, 134, 11));
		BTN_B4.setContentAreaFilled(false);
		BTN_B4.setOpaque(true);
		BTN_B4.setBorderPainted(false);
		contentPane.add(BTN_B4, "6, 12, 2, 2");
		
		JButton BTN_C4 = new JButton("");
		BTN_C4.setBackground(new Color(255, 248, 220));
		BTN_C4.setContentAreaFilled(false);
		BTN_C4.setOpaque(true);
		BTN_C4.setBorderPainted(false);
		contentPane.add(BTN_C4, "8, 12, 2, 2");
		
		JButton BTN_D4 = new JButton("");
		BTN_D4.setBackground(new Color(184, 134, 11));
		BTN_D4.setContentAreaFilled(false);
		BTN_D4.setOpaque(true);
		BTN_D4.setBorderPainted(false);
		contentPane.add(BTN_D4, "10, 12, 2, 2");
		
		JButton BTN_E4 = new JButton("");
		BTN_E4.setBackground(new Color(255, 248, 220));
		BTN_E4.setContentAreaFilled(false);
		BTN_E4.setOpaque(true);
		BTN_E4.setBorderPainted(false);
		contentPane.add(BTN_E4, "12, 12, 2, 2");
		
		JButton BTN_F4 = new JButton("");
		BTN_F4.setBackground(new Color(184, 134, 11));
		BTN_F4.setContentAreaFilled(false);
		BTN_F4.setOpaque(true);
		BTN_F4.setBorderPainted(false);
		contentPane.add(BTN_F4, "14, 12, 2, 2");
		
		JButton BTN_G4 = new JButton("");
		BTN_G4.setBackground(new Color(255, 248, 220));
		BTN_G4.setContentAreaFilled(false);
		BTN_G4.setOpaque(true);
		BTN_G4.setBorderPainted(false);
		contentPane.add(BTN_G4, "16, 12, 2, 2");
		
		JButton BTN_H4 = new JButton("");
		BTN_H4.setBackground(new Color(184, 134, 11));
		BTN_H4.setContentAreaFilled(false);
		BTN_H4.setOpaque(true);
		BTN_H4.setBorderPainted(false);
		contentPane.add(BTN_H4, "18, 12, 2, 2");
		
		JLabel lblNewLabel_2_3 = new JLabel("3");
		lblNewLabel_2_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_3.setForeground(new Color(255, 250, 250));
		lblNewLabel_2_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_2_3, "2, 14");
		
		JButton BTN_A3 = new JButton("");
		BTN_A3.setBackground(new Color(184, 134, 11));
		BTN_A3.setContentAreaFilled(false);
		BTN_A3.setOpaque(true);
		BTN_A3.setBorderPainted(false);
		contentPane.add(BTN_A3, "4, 14, 2, 2");
		
		JButton BTN_B3 = new JButton("");
		BTN_B3.setBackground(new Color(255, 248, 220));
		BTN_B3.setContentAreaFilled(false);
		BTN_B3.setOpaque(true);
		BTN_B3.setBorderPainted(false);
		contentPane.add(BTN_B3, "6, 14, 2, 2");
		
		JButton BTN_C3 = new JButton("");
		BTN_C3.setBackground(new Color(184, 134, 11));
		BTN_C3.setContentAreaFilled(false);
		BTN_C3.setOpaque(true);
		BTN_C3.setBorderPainted(false);
		contentPane.add(BTN_C3, "8, 14, 2, 2");
		
		JButton BTN_D3 = new JButton("");
		BTN_D3.setBackground(new Color(255, 248, 220));
		BTN_D3.setContentAreaFilled(false);
		BTN_D3.setOpaque(true);
		BTN_D3.setBorderPainted(false);
		contentPane.add(BTN_D3, "10, 14, 2, 2");
		
		JButton BTN_E3 = new JButton("");
		BTN_E3.setBackground(new Color(184, 134, 11));
		BTN_E3.setContentAreaFilled(false);
		BTN_E3.setOpaque(true);
		BTN_E3.setBorderPainted(false);
		contentPane.add(BTN_E3, "12, 14, 2, 2");
		
		JButton BTN_F3 = new JButton("");
		BTN_F3.setBackground(new Color(255, 248, 220));
		BTN_F3.setContentAreaFilled(false);
		BTN_F3.setOpaque(true);
		BTN_F3.setBorderPainted(false);
		contentPane.add(BTN_F3, "14, 14, 2, 2");
		
		JButton BTN_G3 = new JButton("");
		BTN_G3.setBackground(new Color(184, 134, 11));
		BTN_G3.setContentAreaFilled(false);
		BTN_G3.setOpaque(true);
		BTN_G3.setBorderPainted(false);
		contentPane.add(BTN_G3, "16, 14, 2, 2");
		
		JButton BTN_H3 = new JButton("");
		BTN_H3.setBackground(new Color(255, 248, 220));
		BTN_H3.setContentAreaFilled(false);
		BTN_H3.setOpaque(true);
		BTN_H3.setBorderPainted(false);
		contentPane.add(BTN_H3, "18, 14, 2, 2");
		
		JLabel lblNewLabel_2_4 = new JLabel("2");
		lblNewLabel_2_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_4.setForeground(new Color(255, 250, 250));
		lblNewLabel_2_4.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_2_4, "2, 16");
		
		JButton BTN_A2 = new JButton("");
		BTN_A2.setBackground(new Color(255, 248, 220));
		BTN_A2.setContentAreaFilled(false);
		BTN_A2.setOpaque(true);
		BTN_A2.setBorderPainted(false);
		contentPane.add(BTN_A2, "4, 16, 2, 2");
		
		JButton BTN_B2 = new JButton("");
		BTN_B2.setBackground(new Color(184, 134, 11));
		BTN_B2.setContentAreaFilled(false);
		BTN_B2.setOpaque(true);
		BTN_B2.setBorderPainted(false);
		contentPane.add(BTN_B2, "6, 16, 2, 2");
		
		JButton BTN_C2 = new JButton("");
		BTN_C2.setBackground(new Color(255, 248, 220));
		BTN_C2.setContentAreaFilled(false);
		BTN_C2.setOpaque(true);
		BTN_C2.setBorderPainted(false);
		contentPane.add(BTN_C2, "8, 16, 2, 2");
		
		JButton BTN_D2 = new JButton("");
		BTN_D2.setBackground(new Color(184, 134, 11));
		BTN_D2.setContentAreaFilled(false);
		BTN_D2.setOpaque(true);
		BTN_D2.setBorderPainted(false);
		contentPane.add(BTN_D2, "10, 16, 2, 2");
		
		JButton BTN_E2 = new JButton("");
		BTN_E2.setBackground(new Color(255, 248, 220));
		BTN_E2.setContentAreaFilled(false);
		BTN_E2.setOpaque(true);
		BTN_E2.setBorderPainted(false);
		contentPane.add(BTN_E2, "12, 16, 2, 2");
		
		JButton BTN_F2 = new JButton("");
		BTN_F2.setBackground(new Color(184, 134, 11));
		BTN_F2.setContentAreaFilled(false);
		BTN_F2.setOpaque(true);
		BTN_F2.setBorderPainted(false);
		contentPane.add(BTN_F2, "14, 16, 2, 2");
		
		JButton BTN_G2 = new JButton("");
		BTN_G2.setBackground(new Color(255, 248, 220));
		BTN_G2.setContentAreaFilled(false);
		BTN_G2.setOpaque(true);
		BTN_G2.setBorderPainted(false);
		contentPane.add(BTN_G2, "16, 16, 2, 2");
		
		JButton BTN_H2 = new JButton("");
		BTN_H2.setBackground(new Color(184, 134, 11));
		BTN_H2.setContentAreaFilled(false);
		BTN_H2.setOpaque(true);
		BTN_H2.setBorderPainted(false);
		contentPane.add(BTN_H2, "18, 16, 2, 2");
		
		JLabel lblNewLabel_2_5 = new JLabel("1");
		lblNewLabel_2_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_5.setForeground(new Color(255, 250, 250));
		lblNewLabel_2_5.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_2_5, "2, 18");
		
		JButton BTN_A1 = new JButton("");
		BTN_A1.setBackground(new Color(184, 134, 11));
		BTN_A1.setContentAreaFilled(false);
		BTN_A1.setOpaque(true);
		BTN_A1.setBorderPainted(false);
		contentPane.add(BTN_A1, "4, 18, 2, 2");
		
		JButton BTN_B1 = new JButton("");
		BTN_B1.setBackground(new Color(255, 248, 220));
		BTN_B1.setContentAreaFilled(false);
		BTN_B1.setOpaque(true);
		BTN_B1.setBorderPainted(false);
		contentPane.add(BTN_B1, "6, 18, 2, 2");
		
		JButton BTN_C1 = new JButton("");
		BTN_C1.setBackground(new Color(184, 134, 11));
		BTN_C1.setContentAreaFilled(false);
		BTN_C1.setOpaque(true);
		BTN_C1.setBorderPainted(false);
		contentPane.add(BTN_C1, "8, 18, 2, 2");
		
		JButton BTN_D1 = new JButton("");
		BTN_D1.setBackground(new Color(255, 248, 220));
		BTN_D1.setContentAreaFilled(false);
		BTN_D1.setOpaque(true);
		BTN_D1.setBorderPainted(false);
		contentPane.add(BTN_D1, "10, 18, 2, 2");
		
		JButton BTN_E1 = new JButton("");
		BTN_E1.setBackground(new Color(184, 134, 11));
		BTN_E1.setContentAreaFilled(false);
		BTN_E1.setOpaque(true);
		BTN_E1.setBorderPainted(false);
		contentPane.add(BTN_E1, "12, 18, 2, 2");
		
		JButton BTN_F1 = new JButton("");
		BTN_F1.setBackground(new Color(255, 248, 220));
		BTN_F1.setContentAreaFilled(false);
		BTN_F1.setOpaque(true);
		BTN_F1.setBorderPainted(false);
		contentPane.add(BTN_F1, "14, 18, 2, 2");
		
		JButton BTN_G1 = new JButton("");
		BTN_G1.setBackground(new Color(184, 134, 11));
		BTN_G1.setContentAreaFilled(false);
		BTN_G1.setOpaque(true);
		BTN_G1.setBorderPainted(false);	
		contentPane.add(BTN_G1, "16, 18, 2, 2");
		
		JButton BTN_H1 = new JButton("");
		BTN_H1.setBackground(new Color(255, 248, 220));
		BTN_H1.setContentAreaFilled(false);
		BTN_H1.setOpaque(true);
		BTN_H1.setBorderPainted(false);
		contentPane.add(BTN_H1, "18, 18, 2, 2");
		
		// Fila A
		tablero[0][0] = BTN_A1;
		tablero[0][1] = BTN_A2;
		tablero[0][2] = BTN_A3;
		tablero[0][3] = BTN_A4;
		tablero[0][4] = BTN_A5;
		tablero[0][5] = BTN_A6;
		tablero[0][6] = BTN_A7;
		tablero[0][7] = BTN_A8;

		// Fila B
		tablero[1][0] = BTN_B1;
		tablero[1][1] = BTN_B2;
		tablero[1][2] = BTN_B3;
		tablero[1][3] = BTN_B4;
		tablero[1][4] = BTN_B5;
		tablero[1][5] = BTN_B6;
		tablero[1][6] = BTN_B7;
		tablero[1][7] = BTN_B8;

		//fila C
		tablero[2][0] = BTN_C1;
		tablero[2][1] = BTN_C2;
		tablero[2][2] = BTN_C3;
		tablero[2][3] = BTN_C4;
		tablero[2][4] = BTN_C5;
		tablero[2][5] = BTN_C6;
		tablero[2][6] = BTN_C7;
		tablero[2][7] = BTN_C8;
		
		//fila D
		tablero[3][0] = BTN_D1;
		tablero[3][1] = BTN_D2;
		tablero[3][2] = BTN_D3;
		tablero[3][3] = BTN_D4;
		tablero[3][4] = BTN_D5;
		tablero[3][5] = BTN_D6;
		tablero[3][6] = BTN_D7;
		tablero[3][7] = BTN_D8;
		
		//fila E
		tablero[4][0] = BTN_E1;
		tablero[4][1] = BTN_E2;
		tablero[4][2] = BTN_E3;
		tablero[4][3] = BTN_E4;
		tablero[4][4] = BTN_E5;
		tablero[4][5] = BTN_E6;
		tablero[4][6] = BTN_E7;
		tablero[4][7] = BTN_E8;
		
		//fila F
		tablero[5][0] = BTN_F1;
		tablero[5][1] = BTN_F2;
		tablero[5][2] = BTN_F3;
		tablero[5][3] = BTN_F4;
		tablero[5][4] = BTN_F5;
		tablero[5][5] = BTN_F6;
		tablero[5][6] = BTN_F7;
		tablero[5][7] = BTN_F8;
		
		//fila G
		tablero[6][0] = BTN_G1;
		tablero[6][1] = BTN_G2;
		tablero[6][2] = BTN_G3;
		tablero[6][3] = BTN_G4;
		tablero[6][4] = BTN_G5;
		tablero[6][5] = BTN_G6;
		tablero[6][6] = BTN_G7;
		tablero[6][7] = BTN_G8;
		
		//fila H
		tablero[7][0] = BTN_H1;
		tablero[7][1] = BTN_H2;
		tablero[7][2] = BTN_H3;
		tablero[7][3] = BTN_H4;
		tablero[7][4] = BTN_H5;
		tablero[7][5] = BTN_H6;
		tablero[7][6] = BTN_H7;
		tablero[7][7] = BTN_H8;
		
		ImageIcon caballoNegro = new ImageIcon(getClass().getResource("/img/caballo.png"));
		Image imgNegro = caballoNegro.getImage().getScaledInstance(tablero[7][1].getWidth(), tablero[7][1].getHeight(), Image.SCALE_SMOOTH);
		tablero[7][1].setIcon(new ImageIcon(imgNegro));
	}

}
