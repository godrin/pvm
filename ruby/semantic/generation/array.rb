class ArrayCode
  def init
    @tmps=@elements.map{|e|tmpVar}
  end
  def elements
    @elements
  end
  def before
    (@elements.map{|e|e.before}+
    (0...@elements.length).to_a.map{|i|s(@tmps[i]+"="+@elements[i].value.to_s)}).inject([]){|a,b|a+b}
  end

  def after
    (@elements.map{|e|e.after}+@tmps.map{|tmp|s("clear "+tmp)}).inject([]){|a,b|a+b}
  end

  def code
    []
  end
  def value
    "["+@tmps.map{|e|e}.join(",")+"]"
  end
  
  def each
    pp caller
    exit
  end
  
  def [](i)
    @elements[i].value
    #raise :FIXME
  end
  
  def size
    @elements.length
  end
end