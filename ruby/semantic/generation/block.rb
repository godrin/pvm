class BlockCode
  def before
    []
  end
  def after
    []
  end
  def code
    @statements.map{|s|s.before+s.code+s.after}.inject([]){|a,b|a+b}
  end
  def value
    @statements[-1].value
  end
end