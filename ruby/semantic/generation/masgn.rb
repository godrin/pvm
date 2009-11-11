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
      assert{[IasgnCode,SplatCode].member?(e.class)}
    }

    i=0
    @names.elements.each{|n|
      if n.is_a?(IasgnCode)
        pp @rvals[i]
        n.value=@rvals[i]
      end
      i+=1
    }

    @names.elements.map{|iasgn|iasgn.code}.inject([]){|a,b|a+b}
  end

  def value
    raise :FIXME
  end
end