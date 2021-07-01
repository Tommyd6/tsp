//This section imports the used libaries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class TSP {
    private static Frame frame;// the frame runs the code
    public static void main(String args[]){
        frame=new Frame();// this creates the gui frame
    }  
}
// this class is what runs the gui
class Frame extends JFrame{
    JFrame frame;
    
    MapPanel map;// this panel does all the drawing of the map and on it
    JLabel MapPanelName;// this is a label for the map
    
    JPanel InputPanel;//this panel is where the features for input is show
    JLabel InputPanelName;// this is label for input
    JTextArea Input;//this is the text area for the input to inputted to
    
    JPanel OutputPanel;// this panel is where the output for output is shown
    JLabel OutputPanelName;// this is label for output panel
    JTextArea Output;//this the text Area for where the 
    
    JButton[] Buttons;//this array holds the buttond
    JSlider[] Sliders;//this array holds the slider
    JLabel[] SlidersLabels;//this array holds the Labels for the sliders
    
    // this variables are used for route making
    double[][] distances;//distance matrix
    cord[] arr;// object array that holds the co-ordinatine information
    route bound;// this is the route
    int waitTime;// this is time between updates 1/fps
    double runTime;// this is how long the code can run for (watchAlgorithm only)
    int lines;// how many lines in input
    boolean Delay;// to show algorithm or not
    //JFrame constructor
    public Frame(){
        frame=new JFrame("Route Planner");// names the jFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// sets the thread to exit when window closed
        frame.setBounds(0,0,1500,800);// sets the size of the frame and it co-ordinates
        frame.getContentPane().setBackground(new Color(226, 36, 36));// sets the background of the frame(Apache Pizza Red)
        frame.setLayout(null);
        frame.setVisible(true);// sets frame to be visible
        createInputPanel();
        createOutputPanel();
        createButtons();
        createSliders();
        runTime=70*1000;// sets the default run time to be mid of Time slider
        waitTime=1000/50;//sets the default waitTime to 1/(mid of FPS slider)
        createMapPanel();
    }
    // creates the map section
    private void createMapPanel(){
        map=new MapPanel();// creats the map Panel object
        
        MapPanelName=new JLabel("Map Points");// makes the label for panel
        MapPanelName.setBounds(20,0,100,20);// sets the co-ordinates for the label
        MapPanelName.setVisible(true);// sets the Label to be visible
        map.setVisible(true);// sets the map object to be visible
        
        //adds mapPanel and Label to frame;
        frame.add(MapPanelName);
        frame.add(map);
    }
    // creates the output section
    private void createOutputPanel(){
        // creates the outPut panel
        OutputPanel=new JPanel();
        OutputPanel.setBounds(20,640,780,100);// sets the co-ordinates for the panel
        OutputPanel.setBackground(new Color(253, 195, 36));//sets the panel to be Apache Pizza Yellow
        OutputPanel.setVisible(true);// sets the OutPanel to be visible
      
        // creates the Label for the Output
        OutputPanelName=new JLabel("Output order");
        OutputPanelName.setBounds(20,620,100,20);
        OutputPanelName.setVisible(true);
        
        //  creates the text Area for output
        Output=new JTextArea(800,100);
        Output.setLineWrap(true);
        Output.setVisible(true);
        
        // adds a scroll to the  TextArea
        JScrollPane scroll = new JScrollPane (Output);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(40,650,740,80);// sets the co-ordinates for the TextArea
        scroll.setVisible(true);
        
        //adds componemnts to the frame 
        frame.add(scroll);
        frame.add(OutputPanelName);
        frame.add(OutputPanel);
    }
    // creates the input section
    private void createInputPanel(){
        // creates the outPut panel
        InputPanel=new JPanel();
        InputPanel.setBounds(1040,20,420,600);
        InputPanel.setBackground(new Color(253, 195, 36));
        InputPanel.setVisible(true);
      
         // creates the Label for the input
        InputPanelName=new JLabel("Input Address");
        InputPanelName.setBounds(1040,0,100,20);
        InputPanelName.setVisible(true);

         //  creates the text Area for input
        Input=new JTextArea(800,100);
        Input.setLineWrap(true);
        Input.setVisible(true);
        
        // adds a scroll to the  TextArea
        JScrollPane scroll2 = new JScrollPane (Input);
        scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll2.setBounds(1050,40,400,560);
        scroll2.setVisible(true);
        
        //adds componemnts to the frame 
        frame.add(scroll2);
        frame.add(InputPanelName);
        frame.add(InputPanel);
    }
    // create the buttons
    private void createButtons(){
        //initalizes the button array
        JButton[] Buttons=new JButton[2];
        
        //creats the button for compute Algorithm
        Buttons[0]=new JButton();
        Buttons[0].setBounds(820,640 ,200,100);
        Buttons[0].setText("Click here to compute Route");
        Buttons[0].setBackground(new Color(253, 195, 36));//Apache Pizza Yellow
        Buttons[0].setForeground(new Color(226, 36, 36));//sets text colour(Apache Pizza Red)
        Buttons[0].setBorder(BorderFactory.createEtchedBorder());//adds a visual effect to the button
        
        //creates the button for watch algorthim
        Buttons[1]=new JButton();
        Buttons[1].setBounds(1040,640 ,200,100);
        Buttons[1].setText("Watch Algorithm");
        Buttons[1].setBackground(new Color(253, 195, 36));//Apache Pizza Yellow
        Buttons[1].setForeground(new Color(226, 36, 36));//sets text colour(Apache Pizza Red)
        Buttons[1].setBorder(BorderFactory.createEtchedBorder());
        
        //adds functionality to buttons
        //adds funcionality for compute Route button
        Buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Delay=false;// used in if statement for taking quick of show algorithim path
                functionality();// this function computes the route
            }
    	});
        
        //adds funcionality for watch Algorthm button
        Buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Delay=true;// used in if statement for taking quick of show algorithim path
                functionality();// this function computes the route
            }
    	});
        
        //adds components to frame
        frame.add(Buttons[0]);
        frame.add(Buttons[1]);
    }
    // creates Sliders
    private void createSliders(){
        //intializes arrays for slider and slider labels
        JSlider[] Sliders=new JSlider[2];
        JLabel[] SlidersLabels=new JLabel[2];
        
        //creates Labels
        SlidersLabels[0]=new JLabel("Set FPS");
        SlidersLabels[1]=new JLabel("Set Run Time in seconds");
        SlidersLabels[0].setVisible(true);
        SlidersLabels[1].setVisible(true);
        SlidersLabels[0].setBounds(1260,620,200,20);
        SlidersLabels[1].setBounds(1260,680,200,20);
        
        //Creates the sliders
        
        // this Slider is the FPS slider
        Sliders[0]=new JSlider(1,99);//the FPS can be set between 1-99
        Sliders[0].setBounds(1260,640 ,200,40);
        
        //sets the slider tracks,ticks and labels
        Sliders[0].setPaintTrack(true);
        Sliders[0].setPaintTicks(true);
        Sliders[0].setPaintLabels(true);
        
        Sliders[0].setMajorTickSpacing(49);// space between big Major FPS(1,50,99)
        Sliders[0].setVisible(true);
        Sliders[0].setBackground(new Color(253, 195, 36));//Apache Pizza Yellow
        
        //Slider for timer
        Sliders[1]=new JSlider(10,130);
        Sliders[1].setBounds(1260,700 ,200,40);
        Sliders[1].setPaintTrack(true);
        Sliders[1].setPaintTicks(true);
        Sliders[1].setPaintLabels(true);
        Sliders[1].setMajorTickSpacing(20);
        Sliders[1].setVisible(true);
        Sliders[1].setBackground(new Color(253, 195, 36));
        
        // adds Functionality to Sliders
        
        //This sets the delay time between frames(1/FPS) using the slider
        Sliders[0].addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent a) {
                waitTime=(int)((1000.0/Sliders[0].getValue()));// this get the inverse of FPS(1000 used as code works in milliseconds)
             }
        });
        
        //This sets the Amount of time the code can run for using the slider
        Sliders[1].addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent a) {
                runTime=(int)(Sliders[1].getValue()*1000);// this sets run time (1000 used as code works in milliseconds)
             }
        });
        
        //adds components to frame
        frame.add(Sliders[0]);
        frame.add(Sliders[1]);
        frame.add(SlidersLabels[0]);
        frame.add(SlidersLabels[1]);
    }
    // this function computes the route
    private void functionality(){
        lines = Input.getLineCount();//gets amount of lines in input
        double startTime=System.currentTimeMillis();// used to see when button was hit
        InputSetup(lines);// sets up the object array for input
        fillDistanceMatrix(lines);//gets the distance matrix
        byte[] Order=new byte[lines+1];//Route order (used for graphics)
        
        // this if statement choose which path to take depending on which button was pressed
        if(Delay){
            watchAlgorithim(Order,startTime);
        }else{
             noWaitTime(Order);
        }
        Output.setText(bound.getRouteOrder());//adds string to output text area
    }
    
    // this makes the thread sleep for wait time acting like a delay
    private void waiting(int waitTime){
        try
        {
            Thread.sleep(waitTime);
        }
            catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
    
    // this oputs the input into the co-ordinate array
    private void InputSetup(int lines){
        arr=new cord[lines+1];// sets the array size to be amount of lines +1 because the input dosent include apache Pizza
        arr[0]=new cord(0,-6.59274,53.38197,0);//adds Apache Pizza to the array
        String[] linesText = Input.getText().split("\n");// gets the text from input Text Area
        
        //loops through the input adding each input to co-ordinate array
        for(int i=0;i<lines;i++){
            String input=linesText[i];// gets line i from string
            int index=input.indexOf(',');// This variable holds where the commas are
            int order=Integer.parseInt(input.substring(0,index));// turns the first number in line to be order
            input=input.substring(index+1);// gets rid of number and its comma from string
            index=input.indexOf(',');
            String address =input.substring(0,index);//gets address(not used in code)
            input=input.substring(index+1);
            index=input.indexOf(',');
            double bad_mins=Integer.parseInt(input.substring(0,index));// gets the mins since order
            input=input.substring(index+1);
            index=input.indexOf(',');
            double Y= Double.parseDouble(input.substring(0,index));//gets x co-ordinates
            double X= Double.parseDouble(input.substring(index+1));//gets y co-ordinates
            arr[i+1]=new cord(order,X,Y,bad_mins);//adds infor to array
        }
    }
    // this fills up the distance matrix(kms)
    private void fillDistanceMatrix(int lines){
        distances=new double[lines+1][lines+1];// makes distance matrix
        for(int i=0;i<lines+1;i++){
            for(int j=0;j<lines+1;j++){
                    distances[i][j]=arr[i].time(arr[j]);//gets the time between point i and j
            }
        }
    }
    // this is the section of code that gets the code without showing the algorithm learn the route
    private void noWaitTime(byte[] Order){
        bound=new route(lines+1);// makes route object
        
        // this gets intial route using furthest Insertion
        for(int i=0;i<lines;i++){
            Order=bound.furthestInsertion(distances,arr);
        }
        // this sets the angry minutes used for testing improvements to be bounds Angry minutes
        bound.setbm(distances,arr);
        byte length=bound.getIn();//gets amount of points in route
        // this loops through the route appling 2 opt then swapping every edge
        //5 was picked as it allowed the program to apply 2 opt
        for(int k=0;k<5;k++){
            for(int i=1;i<=lines;i++){//for loop for where reversing begins in 2opt
                for(int j=i+1;j<=lines;j++){// for loop for where reversing ends in 2opt
                    bound.opt2T(i,j,distances,arr);//applys 2 opt
                }
            }
        }
        map.draw(bound.getRoute(),distances,arr,bound.getRoute(),length,length);//draw route on map
    }
    private void watchAlgorithim(byte[] Order,double startTime){
        bound=new route(lines+1);// makes route object
        
        byte[] Order1=new byte[lines+1];// this saves the old Route used for graphics
        
         // this gets intial route using furthest Insertion
        for(int i=0;i<lines;i++){
            
            System.arraycopy(Order, 0, Order1, 0, lines+1);//updates Order1 to be same as Order
            
            Order=bound.furthestInsertion(distances,arr);//inserts new point
            // breaks if time has been longer that run time
            if(System.currentTimeMillis()-startTime>runTime)
                break;
            
            //gets length of route used to avoid graphics drawing back to apache
            byte length=bound.getIn();
            waiting(waitTime);//adds a delay for FPS
            map.draw(Order,distances,arr,Order1,length,(byte)(length-1));//draw Route and old Route on map
        }
        System.arraycopy(Order, 0, Order1, 0, lines+1);
        
        byte[] Order2=new byte[lines+1];// this holds the route being teseted
        
        System.arraycopy(Order, 0, Order2, 0, lines+1);//sets Order 2 to be same as Order
        
        byte length=bound.getIn();
        bound.setbm(distances,arr);
        loop:// this labels the loop so can break out of it from inside the nested loops
        // this loops through the route appling 2 opt
        //5 was picked as it same as no waitTime one so if the loops dont break you get the same results from both buttons
        for(int k=1;k<5;k++){
            for(int i=1;i<=lines;i++){//for loop for where reversing begins in 2opt
                for(int j=i+1;j<=lines;j++){// for loop for where reversing ends in 2opt
                    // breaks from loops if gone over the time
                    if(System.currentTimeMillis()-startTime>runTime)
                        break loop;
                    
                    waiting(waitTime);//adds a delay for FPS
                    Order2=bound.opt2T(i,j,distances,arr);// applies 2 opt to bound and sets Order2 to be the tested route
                    map.draw(Order1,distances,arr,Order2,length,length);// draws map
                    System.arraycopy(Order, 0, Order1, 0, lines+1);//updates Order1 to Order (useful if an update occured)
                }
            }
        }
        map.draw(bound.getRoute(),distances,arr,bound.getRoute(),length,length);//draws maps
    }
}
// this is the mapPanel object that draws the map and the route
class MapPanel extends JPanel{
    boolean draw;//varible for if the route needs to be drawn or not
    byte[] Order;//This holds route Order
    byte[] checking;//This holds the testing route
    double[][] distances;// distance matrix used for colour of points
    cord[] arr;//holds the cords of the inputs
    byte len1;//amount of points in the route
    byte len2;//amount of points in the testing route
    BufferedImage map;// image of map
    // this holds the shape of the panel
    double xSize=1000;
    double ySize=600;
    // this holds the scale and offsetting of the co-ordinates for drawing points
    double Ax=-6.71261;
    double Ay=53.41318;
    double Cx=-6.45509;
    double Cy=53.28426;
    double xScale=xSize/(Cx-Ax);
    double yScale=ySize/(Ay-Cy);
    
    MapPanel(){
        draw=false;
        setBounds(20,20,(int)xSize,(int)ySize);
        setBackground(new Color(253, 195, 36));
        setLayout(null);
        setVisible(true);
        // this gets the map image
        try {
            map = ImageIO.read(new File("src/map.png"));
        }catch(IOException e){
            System.out.println("ERROR");
        }
    }
    // this method is called when the routes are to be drawn on maps
    public void draw(byte[] Order,double[][] distances,cord[] arr,byte[] checking,byte len1,byte len2){
        this.Order=Order;
        this.distances=distances;
        this.arr=arr;
        this.len1=len1;
        this.len2=len2;
        this.checking=checking;
        draw=true;
        paintImmediately(0,0,1000,600);// paint immediately was used as repaint would wait to code was finished till updating
    }
    // this draws the components onto the pannel
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);
        g2.setStroke(new BasicStroke(3));//sets route line size
        g2.drawImage(map, 0, 0,1000 ,600 , Color.BLACK, null);//draws map
        if(!draw){// if route dosent have to be drawn the function is done
            return;
        }
        //Starting points for Routes
        int x1=(int)((-6.59274-Ax)*xScale);
        int y1=(int)((Ay-53.38197)*yScale);
        g2.setColor(Color.MAGENTA);
        //Draws the testing Route
        for(int i=0;i<len2;i++){
            // next point in Route
            int x2=(int)((arr[checking[i]].getX()-Ax)*xScale);
            int y2=(int)((Ay-arr[checking[i]].getY())*yScale);
            //draws Line from current point to next
            g2.drawLine(x1, y1, x2, y2);
            //updates current point
            x1=x2;
            y1=y2;
        }
        x1=(int)((-6.59274-Ax)*xScale);
        y1=(int)((Ay-53.38197)*yScale);
        g2.setColor(Color.BLUE);
        //Draws Route (done after so testing route difference are only thing shown
        for(int i=1;i<len1;i++){
            // next point in Route
            int x2=(int)((arr[Order[i]].getX()-Ax)*xScale);
            int y2=(int)((Ay-arr[Order[i]].getY())*yScale);
            //draws Line from current point to next
            g2.drawLine(x1, y1, x2, y2);
            //updates current point
            x1=x2;
            y1=y2;
        }
        x1=(int)((-6.59274-Ax)*xScale);
        y1=(int)((Ay-53.38197)*yScale);
        g2.setColor(Color.BLACK);
        g2.fillRect(x1-4, y1-4, 8, 8);
        double distance=0;
        //Draws coloured dots at points on Route Red or Green depending on if the customer was waiting over 30 mins
        for(int i=1;i<arr.length;i++){
            int x2=(int)((arr[Order[i]].getX()-Ax)*xScale);
            int y2=(int)((Ay-arr[Order[i]].getY())*yScale);
            distance+=distances[Order[i-1]][Order[i]];//updates distance to current point
            g2.setColor(distance+arr[Order[i]].getBm()>30?Color.RED:Color.GREEN);//sets colour for dot
            g2.drawOval(x2, y2,3, 3);//draws dot
            x1=x2;
            y1=y2;
        }
        x1=(int)((-6.59274-Ax)*xScale);
        y1=(int)((Ay-53.38197)*yScale);
        g2.setColor(Color.BLACK);
        g2.fillRect(x1-4, y1-4, 8, 8);//draws a square at ApachePizza
        
        //draws outlines around the points
        for(int i=1;i<arr.length;i++){
            int x2=(int)((arr[i].getX()-Ax)*xScale);
            int y2=(int)((Ay-arr[i].getY())*yScale);
            g2.drawOval(x2-2, y2-2, 7, 7);//draws outline of dot
        }
    }
}
// this object class holds the order,x,y and minutes since order of each point
class cord{
    int o;//order
    double x,y,bm;//Longitude,Latitude.Minutes since order
    //this is the general constructor of the class
    cord(int o,double x,double y,double bm){
        this.x=x;
        this.y=y;
        this.o=o;
        this.bm=bm;
    }
    //getters for the class
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getOrder(){
        return o;
    }
    public double getBm(){
        return bm;
    }
    // this function gets the distance between this cord and another cord
    public double time(cord b){
        int R = 6371; // Radius of the earth
        double lat1 =y;
        double lon1 =x;
        double lat2 =b.y;
        double lon2 =b.x;
        double latDistance = (lat2-lat1)* Math.PI / 180;
        double lonDistance = (lon2-lon1)* Math.PI / 180;
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + 
        Math.cos((lat1)* Math.PI / 180) * Math.cos(lat2* Math.PI / 180) * 
        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}
// this class holds the route and has function for making the route
class route{
    byte[] Route;// this is an array of the route
    boolean[] inputted;// this boolean checks to see if a point was already inputted
    double Distance;// this is the distance of the route
    byte amount;// this is how many points in route when finished
    byte in;// this is how points are already in the route
    double bm;// the is how many angry minutes in the route
    //general constructor
    route(int lines){
        amount=(byte)lines;
        Route=new byte[amount];
        Distance=0;
        in=1;
        inputted=new boolean[amount];
        bm=0;
    }
    //basic getters for the class
    public double getD(){
        return Distance;
    }
    public byte getIn(){
        return in;
    }
    public double getbm(){
        return bm;
    }
    public byte[] getRoute(){
        return Route;
    }
    //This gets the Route order as a string
    public String getRouteOrder(){
        String order="";
        for(int i=1;i<in-1;i++){// goes to in-2 to avoid an extra comma and starts at 1 to avoid 0 being part of string
            order+=Route[i]+",";
        }
        order+=Route[in-1];
        return order;
    }
    //This method checks how many angry minutes are in the route and sets it
    public void setbm(double[][] da,cord[] arr){
        double d=0;
        for(int i=0;i<amount-1;i++){
            double temp=arr[Route[i+1]].getBm();
            d+=da[Route[i]][Route[i+1]];
            temp+=d;
            temp=temp>30?temp-30:0;
            bm+=temp;
        }
    }
    // this function checks how many angry minutes in a test route(used for testing 2opt and move edge)
    private double testbm(double[][] da,cord[] arr,byte[] test){
        double d=0;
        double testbm=0;
        //goes through test route
        for(int i=0;i<amount-1;i++){
            double temp=arr[test[i+1]].getBm();//gets minutes since order of current point
            d+=da[test[i]][test[i+1]];//gets distance at current point
            temp+=d;//adds distance to minutes since order
            temp=temp>30?temp-30:0;//if minutes since order and distance is greater than 30 the extra time is added
            testbm+=temp;
        }
        return testbm;
    }
    // this checks to see if test route is better than current
    private void test(byte[] test,byte[] change,double[][] da,cord[] arr){
        double testBm=testbm(da,arr,test);//gets bm of test Route
        if(testBm<bm){// check if test route is better than current route
            bm=testBm;//sets angry mins as test route angry minutes
            System.arraycopy(test, 0, change, 0, amount);// makes Route the same as testRoute
        }
    }
    // this finds the furthest point from any point not in the route already and insert it in shortest way possible
    public byte[] furthestInsertion(double[][] da,cord[] arr){
        //sets furthest distance as 0 so even tiny distance will be bigger than it
        double furthest=0;
        byte add=0;//point going to be added to route
        //this two for loops compared the distance between i and j
        for(byte i=0;i<amount;i++){
            for(byte j=1;j<amount;j++){
                if(inputted[j]||i==j){//if j is alrady inputted or i==j we dont care about their distance so continue to next loop
                    continue;
                }    
                if(furthest<=da[Route[i]][j]+arr[j].getBm()){//if furthest is less than distance +order time of j we set that as new furthest and point to be added
                    furthest=da[Route[i]][j];
                    add=j;
                }
            }
        }
        nearestInsert(da,add,arr);//adds the furthest point in the shortest way
        return Route;// returns route for graphics
    }
    // this method finds the cheapest insertion of point to the the route
    private void nearestInsert(double[][] da,byte add,cord[] arr){
        double shortest=da[add][Route[in-1]];//sets shortest distance as the end of route
        byte position=in;//sets position for add to be placed in as end 
        //goes through route and see which place would the add have the smallest effect on distance
        for(byte i=0;i<in;i++){
            if(shortest>da[add][Route[i]]+da[add][Route[i+1]]-da[Route[i]][Route[i+1]]){//checks to see if shortest is greater than the distance added by adding the point here
                shortest=da[add][Route[i]]+da[add][Route[i+1]]-da[Route[i]][Route[i+1]];//updates shortest
                position=(byte)(i+1);//updates position
            }
        }
        Distance+=shortest;//adds shortest to distance
        add(add,position);//adds point to route in position
    }
    // this method adds a point to route at a given index
    private void add(byte add,byte index){
        inputted[add]=true;
        // shifts all points above the index up 1 (was implemented before I learned about System.arraycopy which would of been a more efficent way to do this)
        for(byte i=in;i>index;i--){
            Route[i]=Route[i-1];
        }
        //adds point to eroute
        Route[index]=add;
        in++;
    }
    //this method performs 2 opt between two edges
    public byte[] opt2T(int a,int b,double da[][],cord[] arr){
        byte[] test=new byte[amount];
        System.arraycopy(Route, 0, test, 0, amount);//copies Route to test
        Reverse(a,b,test);//Reverse the section between a and b in test route
        test(test,Route,da,arr);//test to see if test route an imrovement and if so makes it
        return test;//returns test for graphics
    }
    // this section reverse a given array between the section a and b
    private void Reverse(int a,int b,byte[] temp){
        for(int i=a;i<b+1;i++){
            temp[b-i+a]=Route[i];
        }
    }
}