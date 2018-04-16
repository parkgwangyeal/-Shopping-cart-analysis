import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
 
public class SA {
   public static void main(String[] args) throws IOException {      
      HashMap<String, Integer> Couple = new  HashMap<>();   
      HashMap<String, Integer> GoodsFrequncy = new  HashMap<>();               
      HashMap<String, Double> Support = new  HashMap<>(); 
      HashMap<String, Double> Connfidence = new  HashMap<>(); 
      ArrayList<String> Goods = new ArrayList<>();
      ArrayList<String> SortedCouple = new ArrayList<String>();
      final JFileChooser fc = new JFileChooser();      
      File file;   
      if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
         file = fc.getSelectedFile();
      else  {
         JOptionPane.showMessageDialog(null, "파일을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
         return;
      }         
    	  Scanner sc = new Scanner(System.in);
          System.out.print("Top-k의 값을 입력해주세요 : ");
          int Showcouple = sc.nextInt();
          System.out.print("신뢰도를 입력해주세요 : ");
          double connfidence = sc.nextFloat();
          System.out.print("지지도를 입력해주세요 : ");
          double support = sc.nextFloat();
          System.out.println();           
          sc.close();
          Scanner fs = new Scanner(file);   
          int  basket = fs.nextInt();
          while(fs.hasNext()) {
        	  fs.nextInt();
             int goodsNumber = fs.nextInt();
             for(int number = 0 ; number < goodsNumber; number++) {
                String goods = fs.next();
                Goods.add(number, goods);                    
                if(!GoodsFrequncy.containsKey(goods)) GoodsFrequncy.put(goods, 1);                   
                else GoodsFrequncy.put(goods, GoodsFrequncy.get(goods)+1);                     
             }                    
             Collections.sort(Goods);                            
             for(int frount = 0 ; frount < Goods.size(); frount++) {
                for(int back = frount+1 ; back< Goods.size() ; back++) {      
                   String couple = Goods.get(frount)+","+Goods.get(back);           
                   if(!Couple.containsKey(couple))   Couple.put(couple, 1);                       
                   else Couple.put((couple), Couple.get(couple)+1);                                 
                }
             }                  
             Goods.clear();
          }
          fs.close();
          SortedCouple.addAll(Couple.keySet());
          Collections.sort(SortedCouple, new Frequency(Couple));
          String get= SortedCouple.get(Showcouple-1);
          for(int i =0;i<SortedCouple.size();i++){
        	  if(Couple.get(SortedCouple.get(i))>=Couple.get(get)){
        		  System.out.println(i+1+": ["+SortedCouple.get(i)+"]"+" 갯수 "+Couple.get(SortedCouple.get(i)));
        	  }
          }
          for(int i = 0;i<SortedCouple.size();i++){ 
        	 String word=SortedCouple.get(i);
             Integer value = Couple.get(word);
             String frount = word.split(",")[0];
             String back = word.split(",")[1];
             if((double)value/basket>support&&(double)Couple.get(word)/GoodsFrequncy.get(frount)>connfidence){
                Support.put(frount+"->"+back, (double)value/basket);
                Support.put(back+"->"+frount, (double)value/basket);
                Connfidence.put(frount+"->"+back,(double)Couple.get(word)/GoodsFrequncy.get(frount));
                Connfidence.put(back+"->"+frount,(double)Couple.get(word)/GoodsFrequncy.get(back));
                System.out.println(frount+"->"+back+" 지지도 = "+Support.get(frount+"->"+back)+" 신뢰도 = "+Connfidence.get(frount+"->"+back));
                System.out.println(back+"->"+frount+" 지지도 = "+Support.get(back+"->"+frount)+" 신뢰도 = "+Connfidence.get(back+"->"+frount));
             }
          }         
       }             
} 
class Frequency implements Comparator<String> {
    HashMap<String, Integer> HM;
     public Frequency(HashMap<String, Integer> HM) {
        this.HM = HM;
     }
     public int compare(String couple1, String couple2) {    
      return HM.get(couple2).compareTo(HM.get(couple1));
     }
  }