class ArrayCode
  def elements
    @elements
  end
  def before
    @elements.map{|e|e.before}.inject([]){|a,b|a+b}
  end

  def after
    @elements.map{|e|e.after}.inject([]){|a,b|a+b}
  end

  def code
    raise 1
  end
  def value
    "["+@elements.map{|e|e.value}.join(",")+"]"
  end
end