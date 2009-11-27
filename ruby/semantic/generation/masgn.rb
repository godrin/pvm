# multi assign
class MasgnCode
  def init
    if @rval
      @rvals=@rval.elements(@names.elements.length)
    end
  end

  def before
    []
  end

  def after
    []
  end

  def code
    
    if @rval.nil?
      return @names.elements.map{|e|s("#{e}=nil")}.inject([]){|a,b|a+b}
    end
    
    @names.elements.each{|e|
      pp "KLASS:",e.class
      assert{[LasgnCode,IasgnCode,SplatCode].member?(e.class)}
    }

    i=0
    @names.elements.each{|n|
      if n.is_a?(IasgnCode)
        n.value=@rvals[i]
      end
      i+=1
    }

    @names.elements.map{|iasgn|iasgn.code}.inject([]){|a,b|a+b}
  end
  
  def as_parameters
    @names.value
  end

  def value
    raise :FIXME
  end
end