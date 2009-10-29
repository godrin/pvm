# multi assign
class MasgnCode
  def before
    []
  end

  def after
    []
  end

  def code
    @names.elements.each{|e|
      assert{[IasgnCode,SplatCode].member?(e.class)}
    }

    i=0
    @names.each{|n|
      if n.is_a?(IasgnCode)
        n.value=@rval[i]
      end
      i+=1
    }

    @names.map{|iasgn|iasgn.code}.inject([]){|a,b|a+b}
  end

  def value
    raise :FIXME
  end
end