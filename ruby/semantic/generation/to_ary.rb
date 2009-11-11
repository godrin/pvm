class To_aryCode
  
  class MyCode
    def initialize(p,val,arg)
      @parent=p
      @val=val
      @arg=arg
      
      @t=p.tmpVar
    end
    def before
      s("#{@t}=#{@val}[#{@arg}]")
    end
    def value
      @t
    end
    def after
      s("clear #{@t}")
    end
    
    def s(str)
      @parent.s(str)
    end
  end
  
  def elements(count)
    (0...count).to_a.map{|i|MyCode.new(self,@rval,i)}
  end
end